package com.eihabitat.eihabitat_server.controller;

import com.eihabitat.eihabitat_server.dto.request.ApiResponse;
import com.eihabitat.eihabitat_server.dto.request.UserCreationReq;
import com.eihabitat.eihabitat_server.dto.request.UserUpdateReq;
import com.eihabitat.eihabitat_server.dto.response.SearchResponse;
import com.eihabitat.eihabitat_server.dto.response.UserDemoResponse;
import com.eihabitat.eihabitat_server.dto.response.UserResponse;
import com.eihabitat.eihabitat_server.entity.User;
import com.eihabitat.eihabitat_server.service.EmailService;
import com.eihabitat.eihabitat_server.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    UserService userService;
    EmailService emailService;

    @PostMapping
    ApiResponse<String> createUser(@RequestBody @Valid UserCreationReq req) throws MessagingException {
        ApiResponse<String> resp = new ApiResponse<>();
        resp.setData(userService.confirmEmail(req));
        return resp;
    }

    @GetMapping
    ApiResponse<List<UserResponse>>  getUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("email : {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        ApiResponse<List<UserResponse>> resp = new ApiResponse<>();
        resp.setData(userService.getUsers());
        return resp;
    }


    @PutMapping()
    ApiResponse<UserResponse> updateUser(@RequestBody UserUpdateReq req) {
        ApiResponse<UserResponse> resp = new ApiResponse<>();
        resp.setData(userService.updateUser( req));
        return resp;
    }

    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return "User deleted";
    }

    @GetMapping("/myInfo")
    ApiResponse<UserResponse> getMyInfo() {
        ApiResponse<UserResponse> resp = new ApiResponse<>();
        resp.setData(userService.getMyInfo());
        return resp;
    }

    @GetMapping("/{userProfileName}")
    ApiResponse<UserResponse> getUserProfile(@PathVariable String userProfileName) {
        ApiResponse<UserResponse> resp = new ApiResponse<>();
        resp.setData(userService.getUserInfo(userProfileName));
        return resp;
    }

    @GetMapping("demo/{email}")
    ApiResponse<UserDemoResponse> getDemoUserInfo(@PathVariable String email) {
        ApiResponse<UserDemoResponse> resp = new ApiResponse<>();
        resp.setCode(1000);
        resp.setData(userService.getUserDemo(email));
        return resp;
    }

    @GetMapping("search/{username}")
    ApiResponse<Set<SearchResponse>> searchUser(@PathVariable String username) {
        ApiResponse<Set<SearchResponse>> resp = new ApiResponse<>();
        resp.setData(userService.searchByUsername(username));
        return resp;
    }

}
