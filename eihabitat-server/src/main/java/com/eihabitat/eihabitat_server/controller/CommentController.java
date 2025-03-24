package com.eihabitat.eihabitat_server.controller;

import com.eihabitat.eihabitat_server.dto.request.*;
import com.eihabitat.eihabitat_server.dto.response.CommentResponse;
import com.eihabitat.eihabitat_server.service.CommentService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/comment")
public class CommentController {
    CommentService commentService;

    @PostMapping("/{userId}")
    public ApiResponse<CommentResponse> addComment(@PathVariable String userId, @RequestBody CommentCreationReq commentCreationReq) {
        ApiResponse resp = new ApiResponse();
        resp.setCode(1000);
        resp.setData(commentService.addComment(commentCreationReq, userId));
        return resp;
    }

    @PostMapping("/getComment")
    public ApiResponse<Set<CommentResponse>> getComment(@RequestBody GetCommentReq commentReq) {
        ApiResponse resp = new ApiResponse();
        resp.setCode(1000);
        resp.setData(commentService.getAllComment(commentReq));
        return resp;
    }


    //    @PreAuthorize("@postOwner.isCommentOwner(#id)")
    @PutMapping("/update/{commentId}")
    public ApiResponse<CommentResponse> updateComment(@PathVariable String commentId,
                                                      @Valid @RequestBody CommentUpdateReq commentRequest) {
        ApiResponse resp = new ApiResponse();
        resp.setCode(1000);
        resp.setData(commentService.updateComment(commentId, commentRequest));
        return resp;
    }

    @PostMapping("/like")
    public ApiResponse<String> like(@RequestBody LikeCommentRequest request) {
        ApiResponse resp = new ApiResponse();
        resp.setCode(1000);
        resp.setData(commentService.likeComment(request));
        return resp;
    }
}
