package com.eihabitat.eihabitat_server.controller;

import com.eihabitat.eihabitat_server.dto.request.ApiResponse;
import com.eihabitat.eihabitat_server.dto.request.SavePostReq;
import com.eihabitat.eihabitat_server.dto.response.AlbumResponse;
import com.eihabitat.eihabitat_server.dto.response.PostOnPersonalWallResponse;
import com.eihabitat.eihabitat_server.service.SavedPostService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/savedPost")
public class SavedPostController {

    SavedPostService savedPostService;

    @GetMapping("/{rootUserId}")
    public ApiResponse<List<PostOnPersonalWallResponse>> getAllSavedPost(@Valid @PathVariable String rootUserId) {
        List<PostOnPersonalWallResponse> result = savedPostService.getAllSavedPosts(rootUserId);
        return  ApiResponse.<List<PostOnPersonalWallResponse>>builder()
                .code(1000)
                .data(result)
                .build();
    }

    @PostMapping()
    public ApiResponse<String> savePost(@Valid @RequestBody SavePostReq req) throws Exception {
        String result = savedPostService.savePost(req);
        return ApiResponse.<String>builder()
                .code(1000)
                .data(result)
                .build();
    }

    @GetMapping("/top4/{userId}")
    public ApiResponse<AlbumResponse> getTop4SavedPosts(@PathVariable String userId) {
        AlbumResponse result = savedPostService.getTop4(userId);
        return ApiResponse.<AlbumResponse>builder()
                .code(1000)
                .data(result)
                .build();
    }
}
