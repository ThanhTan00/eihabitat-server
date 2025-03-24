package com.eihabitat.eihabitat_server.controller;

import com.eihabitat.eihabitat_server.dto.request.ApiResponse;
import com.eihabitat.eihabitat_server.dto.request.UserFollowReq;
import com.eihabitat.eihabitat_server.dto.response.UserFollowerResponse;
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
@RequestMapping("/follow")
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

    @GetMapping("/{username}/followers/{rootUserId}")
    public ApiResponse<List<UserFollowerResponse>> getFollowers(@PathVariable String username, @PathVariable String rootUserId) {
        ApiResponse resp = new ApiResponse();
        resp.setCode(1000);
        resp.setData(followService.getFollowers(username, rootUserId));
        return resp;
    }

    @GetMapping("/{username}/following/{rootUserId}")
    public ApiResponse<List<UserResponse>> getFollowing(@PathVariable String username, @PathVariable String rootUserId) {
        ApiResponse resp = new ApiResponse();
        resp.setCode(1000);
        resp.setData(followService.getFollowing(username, rootUserId));
        return resp;
    }

    @GetMapping("/suggestions/{rootUserId}")
    public ApiResponse<List<UserResponse>> getSuggestions(@PathVariable String rootUserId) {
        ApiResponse resp = new ApiResponse();
        resp.setCode(1000);
        resp.setData(followService.suggestByUserId(rootUserId));
        return resp;
    }

    @GetMapping("/followedMeSuggestions/{rootUserId}")
    public ApiResponse<List<UserResponse>> getFollowedMeSuggestions(@PathVariable String rootUserId) {
        ApiResponse resp = new ApiResponse();
        resp.setCode(1000);
        resp.setData(followService.suggestByFollowedMe(rootUserId));
        return resp;
    }

}
