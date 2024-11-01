package com.eihabitat.eihabitat_server.controller;

import com.eihabitat.eihabitat_server.dto.request.ApiResponse;
import com.eihabitat.eihabitat_server.dto.request.UserFollowReq;
import com.eihabitat.eihabitat_server.dto.response.UserResponse;
import com.eihabitat.eihabitat_server.service.UserFollowService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/follow")
public class UserFollowController {
    private final UserFollowService followService;

    @PostMapping
    public ApiResponse<String> followUser(@RequestBody UserFollowReq requestDto) {
        ApiResponse resp = new ApiResponse();
        resp.setCode(1000);
        resp.setData(followService.followUser(requestDto));
        return resp;
    }

    @PostMapping("/unfollow")
    public ApiResponse<String> unfollowUser(@RequestBody UserFollowReq requestDto) {
        ApiResponse resp = new ApiResponse();
        resp.setCode(1000);
        resp.setData(followService.unfollowUser(requestDto));
        return resp;
    }

    @GetMapping("/{username}/followers")
    public ApiResponse<List<UserResponse>> getFollowers(@PathVariable String username) {
        ApiResponse resp = new ApiResponse();
        resp.setCode(1000);
        resp.setData(followService.getFollowers(username));
        return resp;
    }

    @GetMapping("/{username}/following")
    public ApiResponse<List<UserResponse>> getFollowing(@PathVariable String username) {
        ApiResponse resp = new ApiResponse();
        resp.setCode(1000);
        resp.setData(followService.getFollowing(username));
        return resp;
    }
}
