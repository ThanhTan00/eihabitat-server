package com.eihabitat.eihabitat_server.service;

import com.eihabitat.eihabitat_server.dto.request.CommentCreationReq;
import com.eihabitat.eihabitat_server.dto.request.CommentUpdateReq;
import com.eihabitat.eihabitat_server.entity.Comment;
import com.eihabitat.eihabitat_server.entity.Post;
import com.eihabitat.eihabitat_server.entity.User;
import com.eihabitat.eihabitat_server.exception.AppException;
import com.eihabitat.eihabitat_server.exception.ErrorCode;
import com.eihabitat.eihabitat_server.mapper.CommentMapper;
import com.eihabitat.eihabitat_server.repository.CommentRepository;
import com.eihabitat.eihabitat_server.repository.PostRepository;
import com.eihabitat.eihabitat_server.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CommentService {
    CommentRepository commentRepository;
    PostRepository postRepository;
    UserRepository userRepository;
    CommentMapper commentMapper;

    public Comment addComment(String postId, CommentCreationReq data) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        User user = userRepository.findById(data.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Comment comment = commentMapper.toComment(data);
        comment.setPostId(postId);
        comment.setPost(post);
        comment.setContent(data.getContent());
        comment.setOwnerUsername(data.getUsername());
        comment.setOwnerDisplayName(user.getProfileName());
        comment.setCreationDate(Instant.now());

        return commentRepository.save(comment);
    }

    public Comment updateComment(UUID commentId, CommentUpdateReq data) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_EXISTED));
        commentMapper.updateComment(comment, data);
        comment.setContent(data.getContent());
        return commentRepository.save(comment);
    }
}
