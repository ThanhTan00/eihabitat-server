package com.eihabitat.eihabitat_server.service;

import com.eihabitat.eihabitat_server.dto.request.CommentCreationReq;
import com.eihabitat.eihabitat_server.dto.request.CommentUpdateReq;
import com.eihabitat.eihabitat_server.dto.response.CommentResponse;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CommentService {
    CommentRepository commentRepository;
    PostRepository postRepository;
    UserRepository userRepository;
    CommentMapper commentMapper;

    public CommentResponse addComment(CommentCreationReq data) {
        Post post = postRepository.findById(data.getPostId()).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        //log.info(user.getProfileName());

        Comment comment = commentMapper.toComment(data);
        comment.setPostId(data.getPostId());
        comment.setContent(data.getContent());
        comment.setOwnerId(user.getId());
        comment.setCreationDate(LocalDateTime.now());


        CommentResponse commentResponse = commentMapper.toCommentResponse(commentRepository.save(comment));
        commentResponse.setOwnerProfileName(user.getProfileName());
        commentResponse.setOwnerAvatar(user.getProfileAvatar());

        return commentResponse;
    }

    public Comment updateComment(String commentId, CommentUpdateReq data) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_EXISTED));
        commentMapper.updateComment(comment, data);
        comment.setContent(data.getContent());
        return commentRepository.save(comment);
    }
}
