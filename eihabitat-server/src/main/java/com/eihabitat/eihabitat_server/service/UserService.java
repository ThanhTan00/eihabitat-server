package com.eihabitat.eihabitat_server.service;

import com.eihabitat.eihabitat_server.dto.request.AuthenticationReq;
import com.eihabitat.eihabitat_server.dto.request.UserCreationReq;
import com.eihabitat.eihabitat_server.dto.request.UserUpdateReq;
import com.eihabitat.eihabitat_server.dto.response.UserDemoResponse;
import com.eihabitat.eihabitat_server.dto.response.UserFollowerResponse;
import com.eihabitat.eihabitat_server.dto.response.UserResponse;
import com.eihabitat.eihabitat_server.entity.Role;
import com.eihabitat.eihabitat_server.entity.User;
import com.eihabitat.eihabitat_server.exception.AppException;
import com.eihabitat.eihabitat_server.exception.ErrorCode;
import com.eihabitat.eihabitat_server.mapper.UserMapper;
import com.eihabitat.eihabitat_server.repository.RoleRepository;
import com.eihabitat.eihabitat_server.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    UserFollowService userFollowService;

    public UserResponse createUser(UserCreationReq request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        if (userRepository.existsByProfileName(request.getProfileName()))
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setProfileAvatar("asset/images/default-avatar.png");

        LocalDate date = LocalDate.now();
        user.setSignupDate(date);

        Role userRole = roleRepository.findById("USER").orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        HashSet<Role> userRoles = new HashSet<>();

        userRoles.add(userRole);
        user.setRoles(userRoles);

        return userMapper.toUserResponse(userRepository.save(user));
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
    public User getUserWithRoles(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null){
            Hibernate.initialize(user.getRoles());
        }
        return user;
    }

}
