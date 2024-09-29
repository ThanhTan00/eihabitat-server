package com.eihabitat.eihabitat_server.service;

import com.eihabitat.eihabitat_server.dto.request.CommentMessage;
import com.eihabitat.eihabitat_server.dto.response.CommentResponse;
import com.eihabitat.eihabitat_server.entity.Comment;
import com.eihabitat.eihabitat_server.entity.User;
import com.eihabitat.eihabitat_server.repository.CommentRepository;
import com.eihabitat.eihabitat_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {

    CommentRepository commentRepository;
    UserRepository userRepository;

    public CommentResponse addComment(CommentMessage commentMessage) {
        Comment comment = new Comment();
        comment.setContent(commentMessage.getContent());
        comment.setPostId(commentMessage.getPostId());
        comment.setOwnerId(commentMessage.getOwnerId());
        comment.setCreationDate(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);

        return convertToCommentResponse(savedComment);
    }

    private CommentResponse convertToCommentResponse(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setCreationDate(comment.getCreationDate());

        // Fetch user details
        User user = userRepository.findById(comment.getOwnerId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        response.setOwnerProfileName(user.getProfileName());
        response.setOwnerAvatar(user.getProfileAvatar());

        return response;
    }
}