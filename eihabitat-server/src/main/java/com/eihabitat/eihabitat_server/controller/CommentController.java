package com.eihabitat.eihabitat_server.controller;

import com.eihabitat.eihabitat_server.dto.request.CommentMessage;
import com.eihabitat.eihabitat_server.dto.response.CommentResponse;
import com.eihabitat.eihabitat_server.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @MessageMapping("/comment")
    @SendTo("/topic/comments")
    public CommentResponse addComment(CommentMessage commentMessage) {
        // Call your service method to save the comment
        return commentService.addComment(commentMessage);
    }
}