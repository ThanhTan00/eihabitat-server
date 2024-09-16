package com.eihabitat.eihabitat_server.controller;

import com.eihabitat.eihabitat_server.dto.request.ApiResponse;
import com.eihabitat.eihabitat_server.dto.request.CommentCreationReq;
import com.eihabitat.eihabitat_server.dto.request.CommentUpdateReq;
import com.eihabitat.eihabitat_server.dto.request.PostUpdateReq;
import com.eihabitat.eihabitat_server.dto.response.CommentResponse;
import com.eihabitat.eihabitat_server.dto.response.PostResponse;
import com.eihabitat.eihabitat_server.service.CommentService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/comment")
public class CommentController {
    CommentService commentService;

    @PostMapping()
    public ApiResponse<CommentResponse> addComment(@RequestBody CommentCreationReq commentCreationReq, Principal principal, String postId) {
        ApiResponse resp = new ApiResponse();
        resp.setCode(1000);
        commentCreationReq.setUsername(principal.getName());
        resp.setData(commentService.addComment(postId, commentCreationReq));
        return resp;
    }

    //    @PreAuthorize("@postOwner.isCommentOwner(#id)")
    @PutMapping("/update/{commentId}")
    public ApiResponse<CommentResponse> updateComment(@PathVariable UUID commentId,
                                                      @Valid @RequestBody CommentUpdateReq commentRequest) {
        ApiResponse resp = new ApiResponse();
        resp.setCode(1000);
        resp.setData(commentService.updateComment(commentId, commentRequest));
        return resp;
    }
}
