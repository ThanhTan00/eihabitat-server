package com.eihabitat.eihabitat_server.service;

import com.eihabitat.eihabitat_server.dto.request.UserCreationReq;
import com.eihabitat.eihabitat_server.dto.request.UserUpdateReq;
import com.eihabitat.eihabitat_server.dto.response.UserFollowerResponse;
import com.eihabitat.eihabitat_server.dto.response.UserResponse;
import com.eihabitat.eihabitat_server.entity.EmailConfirmationToken;
import com.eihabitat.eihabitat_server.entity.Role;
import com.eihabitat.eihabitat_server.entity.User;
import com.eihabitat.eihabitat_server.exception.AppException;
import com.eihabitat.eihabitat_server.exception.ErrorCode;
import com.eihabitat.eihabitat_server.mapper.UserMapper;
import com.eihabitat.eihabitat_server.repository.EmailConfirmationRepository;
import com.eihabitat.eihabitat_server.repository.RoleRepository;
import com.eihabitat.eihabitat_server.repository.UserRepository;

import java.nio.charset.StandardCharsets;
import java.util.*;

import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    EmailConfirmationRepository emailConfirmationRepository;
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    UserFollowService userFollowService;
    EmailService emailService;
    BytesKeyGenerator DEFAULT_TOKEN_GENERATOR = KeyGenerators.secureRandom(15);
    Charset US_ASCII = StandardCharsets.US_ASCII;


    public UserResponse createUser(UserCreationReq request) throws MessagingException {
        // Check if email or profile name already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        if (userRepository.existsByProfileName(request.getProfileName())) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }

        // Map the request to a User entity and set additional properties
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setProfileAvatar("asset/images/default-avatar.png");
        user.setSignupDate(LocalDate.now());

        // Retrieve the role and set it to the user
        Role userRole = roleRepository.findById("USER")
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        HashSet<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);
        user.setRoles(userRoles);

        // Save the user to the database before sending the email
        user = userRepository.save(user);

        // Now send the registration confirmation email
        this.sendRegistrationConfirmEmail(user);

        // Return the UserResponse object
        return userMapper.toUserResponse(user);
    }


    public void sendRegistrationConfirmEmail(User user) throws MessagingException {
        // Generate the token
        String tokenValue = new String(Base64.getUrlEncoder().encode(DEFAULT_TOKEN_GENERATOR.generateKey()), US_ASCII);
        EmailConfirmationToken emailConfirmationToken = new EmailConfirmationToken();
        emailConfirmationToken.setToken(tokenValue);
        emailConfirmationToken.setTimestamp(LocalDateTime.now());
        emailConfirmationToken.setExpiryTime(LocalDateTime.now().plusHours(24));
        emailConfirmationToken.setUser(user);
        emailConfirmationRepository.save(emailConfirmationToken);
        // Send email
        emailService.sendConfirmationEmail(emailConfirmationToken);
    }

    public boolean verifyUser(String token) {
        EmailConfirmationToken emailToken = emailConfirmationRepository.findByToken(token);

        if (emailToken == null) {
            throw new IllegalArgumentException("Invalid token.");
        }

        // Check if the token has already been used or verified
        if (emailToken.isVerified()) {
            throw new IllegalStateException("Token already verified.");
        }

        // Check if the token has expired
        if (emailToken.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expired.");
        }

        // Mark the token as verified and save the changes
        emailToken.setVerified(true);
        emailConfirmationRepository.save(emailToken);

        // Mark the associated user as verified
        User user = emailToken.getUser();
        user.setAccountVerified(true);
        userRepository.save(user);

        return true;
    }


    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        log.info("Getting users");
        List<UserResponse> userResponses = new ArrayList<>();
        userRepository.findAll().forEach(user -> {
            userResponses.add(userMapper.toUserResponse(user));
        });
       return userResponses;
    }

    @PostAuthorize("returnObject.email == authentication.name")
    public UserResponse getUser(String id) {
        log.info("Getting user with id {}", id);
        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    public UserResponse updateUser(UserUpdateReq req) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUser(user, req);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public UserResponse getMyInfo () {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return userMapper.toUserResponse(user);
    }

    public UserResponse getUserInfo(String userProfileName) {
        User user = userRepository.findByProfileName(userProfileName).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        List<UserFollowerResponse> listFollowers = userFollowService.getFollowers(user.getProfileName());
        List<UserFollowerResponse> listFollowing = userFollowService.getFollowing(user.getProfileName());
        UserResponse userResponse = userMapper.toUserResponse(user);
        userResponse.setFollowers(listFollowers.size());
        userResponse.setFollowing(listFollowing.size());
        return userResponse;
    }
}
