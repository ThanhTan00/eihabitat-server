package com.eihabitat.eihabitat_server.controller;
import com.eihabitat.eihabitat_server.dto.request.ApiResponse;
import com.eihabitat.eihabitat_server.dto.request.UserLikePostReq;
import com.eihabitat.eihabitat_server.dto.request.UserSavePostReq;
import com.eihabitat.eihabitat_server.dto.response.UserLikePostResponse;
import com.eihabitat.eihabitat_server.dto.response.UserSavePostResponse;
import com.eihabitat.eihabitat_server.service.UserLikePostService;
import com.eihabitat.eihabitat_server.service.UserSavePostService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/Saves")
public class UserSavePostController {

    UserSavePostService userSavePostService;

    @PostMapping("/")
    public ApiResponse<String> savePost(@Valid @RequestBody UserSavePostReq request) {
        ApiResponse<String> resp = new ApiResponse<>();
        resp.setCode(1000);
        resp.setData(userSavePostService.savePost(request));
        return resp;
    }

    @GetMapping("/post/{postId}")
    public ApiResponse<List<UserSavePostResponse>> getSavedPostsForPost(@PathVariable String postId) {
        ApiResponse<List<UserSavePostResponse>> resp = new ApiResponse<>();
        resp.setCode(1000);
        resp.setData(userSavePostService.getSavedPostsForPost(postId));
        return resp;
    }

    @GetMapping("/count/{postId}")
    public ApiResponse<Long> getSaveCountForPost(@PathVariable String postId) {
        ApiResponse<Long> resp = new ApiResponse<>();
        resp.setCode(1000);
        resp.setData(userSavePostService.getSaveCountForPost(postId));
        return resp;
    }

    @GetMapping("/check/{userId}/{postId}")
    public ApiResponse<Boolean> hasUserSavedPost(@PathVariable String userId, @PathVariable String postId) {
        ApiResponse<Boolean> resp = new ApiResponse<>();
        resp.setCode(1000);
        resp.setData(userSavePostService.hasUserSavedPost(userId, postId));
        return resp;
    }
}

