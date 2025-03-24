package com.eihabitat.eihabitat_server.controller;
import com.eihabitat.eihabitat_server.dto.request.ApiResponse;
import com.eihabitat.eihabitat_server.dto.request.UserLikePostReq;
import com.eihabitat.eihabitat_server.dto.response.UserLikePostResponse;
import com.eihabitat.eihabitat_server.service.UserLikePostService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/likes")
public class UserLikePostController {
    UserLikePostService userLikeService;

    @PostMapping
    public ApiResponse<String> likePost(@Valid @RequestBody UserLikePostReq request) {
        ApiResponse resp = new ApiResponse();
        resp.setCode(1000);
        resp.setData(userLikeService.likePost(request));
        return resp;
    }

    @GetMapping("/post/{postId}")
    public ApiResponse<List<UserLikePostResponse>> getLikesForPost(@PathVariable String postId) {
        ApiResponse resp = new ApiResponse();
        resp.setCode(1000);
        resp.setData(userLikeService.getLikesForPost(postId));
        return resp;
    }

    @GetMapping("/count/{postId}")
    public ApiResponse<Long> getLikeCountForPost(@PathVariable String postId) {
        ApiResponse resp = new ApiResponse();
        resp.setCode(1000);
        resp.setData(userLikeService.getLikeCountForPost(postId));
        return resp;
    }

    @GetMapping("/check/{userId}/{postId}")
    public ApiResponse<Boolean> hasUserLikedPost(@PathVariable String userId, @PathVariable String postId) {
        ApiResponse resp = new ApiResponse();
        resp.setCode(1000);
        resp.setData(userLikeService.hasUserLikedPost(userId, postId));
        return resp;
    }
}
