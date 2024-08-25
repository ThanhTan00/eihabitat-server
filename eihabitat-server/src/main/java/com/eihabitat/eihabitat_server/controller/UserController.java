package com.eihabitat.eihabitat_server.controller;

import com.eihabitat.eihabitat_server.dto.request.ApiResponse;
import com.eihabitat.eihabitat_server.dto.request.UserCreationReq;
import com.eihabitat.eihabitat_server.dto.request.UserUpdateReq;
import com.eihabitat.eihabitat_server.dto.response.UserResponse;
import com.eihabitat.eihabitat_server.entity.User;
import com.eihabitat.eihabitat_server.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    UserService userService;

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationReq req) {
        ApiResponse<UserResponse> resp = new ApiResponse<>();
        resp.setData(userService.createUser(req));
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

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId) {
        ApiResponse<UserResponse> resp = new ApiResponse<>();
        resp.setData(userService.getUser(userId));
        return resp;
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateReq req) {
        ApiResponse<UserResponse> resp = new ApiResponse<>();
        resp.setData(userService.updateUser(userId, req));
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

}
