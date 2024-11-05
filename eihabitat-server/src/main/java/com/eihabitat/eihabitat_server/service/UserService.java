package com.eihabitat.eihabitat_server.service;

import com.eihabitat.eihabitat_server.S3Upload.S3Service;
import com.eihabitat.eihabitat_server.dto.request.UserCreationReq;
import com.eihabitat.eihabitat_server.dto.request.UserUpdateReq;
import com.eihabitat.eihabitat_server.dto.response.UserDemoResponse;
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

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
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
import org.springframework.web.multipart.MultipartFile;

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
    S3Service s3Service;
    BytesKeyGenerator DEFAULT_TOKEN_GENERATOR = KeyGenerators.secureRandom(15);
    Charset US_ASCII = StandardCharsets.US_ASCII;

    public String confirmEmail(UserCreationReq request) throws MessagingException {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        if (userRepository.existsByProfileName(request.getProfileName())) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }
        String tokenValue = new String(Base64.getUrlEncoder().encode(DEFAULT_TOKEN_GENERATOR.generateKey()), US_ASCII);
        EmailConfirmationToken emailConfirmationToken = EmailConfirmationToken.builder()
                .email(request.getEmail())
                .profileName(request.getProfileName())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(request.getPassword())
                .expiryTime(LocalDateTime.now().plusHours(24))
                .token(tokenValue)
                .signupDate(LocalDateTime.now())
                .verified(false)
                .build();
        emailConfirmationRepository.save(emailConfirmationToken);
        emailService.sendConfirmationEmail(emailConfirmationToken);
        return "Please check your email to confirm your email address";
    }


    public boolean createUser(UserCreationReq request) throws MessagingException {
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
        // Retrieve the role and set it to the user
        Role userRole = roleRepository.findById("USER")
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        HashSet<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);
        user.setRoles(userRoles);
        user.setSignupDate(LocalDate.now());

        // Save the user to the database before sending the email
        userRepository.save(user);

        // Return the UserResponse object
        return true;
    }

    public boolean verifyUser(String token) throws MessagingException {
        log.info(token);
        EmailConfirmationToken emailToken = emailConfirmationRepository.findEmailConfirmationTokenByToken(token).orElseThrow(() -> new AppException(ErrorCode.EMAIL_CONFIRM_TOKEN_INVALID));
        log.info(emailToken.toString());
        // Check if the token has already been used or verified
        if (emailToken.isVerified()) {
            return false;
        }

        // Check if the token has expired
        if (emailToken.getExpiryTime().isBefore(LocalDateTime.now())) {
            return false;
        }

        // Mark the token as verified and save the changes
        emailToken.setVerified(true);
        emailConfirmationRepository.save(emailToken);

        UserCreationReq request = UserCreationReq.builder()
                .email(emailToken.getEmail())
                .profileName(emailToken.getProfileName())
                .firstName(emailToken.getFirstName())
                .lastName(emailToken.getLastName())
                .password(emailToken.getPassword())
                .build();

        return createUser(request);
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

    public UserDemoResponse getUserDemo(String email) {
        return userMapper.toUserDemoResponse(userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
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

    public UserResponse getUserInfo(String userProfileName, String rootUser) {
        User user = userRepository.findByProfileName(userProfileName).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        List<UserFollowerResponse> listFollowers = userFollowService.getFollowers(user.getProfileName(), rootUser);
        List<UserFollowerResponse> listFollowing = userFollowService.getFollowing(user.getProfileName(), rootUser);
        UserResponse userResponse = userMapper.toUserResponse(user);
        userResponse.setFollowers(listFollowers.size());
        userResponse.setFollowing(listFollowing.size());
        if (listFollowers.stream()
                .anyMatch(person -> person.getProfileName().equalsIgnoreCase(rootUser)))
        {
            userResponse.setFollowedByMe(true);
        }
        if (listFollowing.stream().anyMatch(person -> person.getProfileName().equalsIgnoreCase(rootUser)))
        {
            userResponse.setFollowMe(true);
        }
        return userResponse;
    }

    public String updateAvatar(String userId, MultipartFile image) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        String date = LocalDate.now().toString();
        user.setProfileAvatar(s3Service.uploadFile(image, userId+date+".jpg"));
        userRepository.save(user);
        return user.getProfileAvatar();
    }

}
