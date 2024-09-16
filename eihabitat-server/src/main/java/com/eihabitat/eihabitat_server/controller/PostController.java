package com.eihabitat.eihabitat_server.controller;

import com.eihabitat.eihabitat_server.dto.request.ApiResponse;
import com.eihabitat.eihabitat_server.dto.request.PostContentReq;
import com.eihabitat.eihabitat_server.dto.request.PostCreationReq;
import com.eihabitat.eihabitat_server.dto.request.PostUpdateReq;
import com.eihabitat.eihabitat_server.dto.response.PostContentResponse;
import com.eihabitat.eihabitat_server.dto.response.PostResponse;
import com.eihabitat.eihabitat_server.service.PostService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/post")
public class PostController {
    PostService postService;
    PostContentService postContentService;

    @PostMapping("/postContent")
    public ApiResponse<PostContentResponse> createPostContent(@RequestBody PostContentReq postContentReq) {
        ApiResponse resp = new ApiResponse();
        resp.setCode(1000);
        resp.setData(postContentService.createPostContent(postContentReq));
        return resp;
    }

    @PostMapping
    public ApiResponse<PostResponse> createPost(@RequestBody PostCreationReq postRequest) {
        ApiResponse resp = new ApiResponse();
        resp.setCode(1000);
        resp.setData(postService.createPost(postRequest));
        return resp;
    }
    @PutMapping("/update/{postId}")
    public ApiResponse<PostResponse> updatePost(@PathVariable String postId,
                                           @Valid @RequestBody PostUpdateReq postRequest) {
        ApiResponse resp = new ApiResponse();
        resp.setCode(1000);
        resp.setData(postService.updatePost(postId,postRequest));
        return resp;
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<PostResponse> deletePost(@PathVariable String postId) throws Exception {
        ApiResponse resp = new ApiResponse();
        resp.setCode(1000);
        resp.setData(postService.deletePost(postId));
        return resp;
    }

    @GetMapping("/{postId}")
    public ApiResponse<PostResponse> findPostById(@PathVariable String postId) throws Exception {
        ApiResponse resp = new ApiResponse();
        resp.setCode(1000);
        resp.setData(postService.findPostById(postId));
        return resp;
    }

//    @GetMapping
//    public ResponseEntity<List<Post>> findAllPost() throws Exception {
//        List<Post> posts = postService.findAllPost();
//        return ResponseEntity.ok(posts);
//    }
//
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<Post>> findPostByUserId(@PathVariable String userId) {
//        List<Post> posts = postService.findPostByUserId(userId);
//        return ResponseEntity.ok(posts);
//    }
}
