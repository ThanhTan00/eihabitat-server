package com.eihabitat.eihabitat_server.service;

import com.eihabitat.eihabitat_server.dto.request.CommentCreationReq;
import com.eihabitat.eihabitat_server.dto.request.CommentUpdateReq;
import com.eihabitat.eihabitat_server.dto.request.GetCommentReq;
import com.eihabitat.eihabitat_server.dto.request.LikeCommentRequest;
import com.eihabitat.eihabitat_server.dto.response.CommentResponse;
import com.eihabitat.eihabitat_server.entity.Comment;
import com.eihabitat.eihabitat_server.entity.LikeComment;
import com.eihabitat.eihabitat_server.entity.Post;
import com.eihabitat.eihabitat_server.entity.User;
import com.eihabitat.eihabitat_server.exception.AppException;
import com.eihabitat.eihabitat_server.exception.ErrorCode;
import com.eihabitat.eihabitat_server.mapper.CommentMapper;
import com.eihabitat.eihabitat_server.repository.CommentRepository;
import com.eihabitat.eihabitat_server.repository.LikeCommentRepository;
import com.eihabitat.eihabitat_server.repository.PostRepository;
import com.eihabitat.eihabitat_server.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CommentService {
    CommentRepository commentRepository;
    LikeCommentRepository likeCommentRepository;
    PostRepository postRepository;
    UserRepository userRepository;

    NotificationService notificationService;

    CommentMapper commentMapper;

    public CommentResponse addComment(CommentCreationReq data, String userID) {
        User user = userRepository.findById(userID).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Post post = postRepository.findById(data.getPostId()).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        Comment comment = commentMapper.toComment(data);
        comment.setPostId(data.getPostId());
        comment.setContent(data.getContent());
        comment.setOwnerId(user.getId());
        comment.setCreationDate(LocalDateTime.now());

        CommentResponse commentResponse = commentMapper.toCommentResponse(commentRepository.save(comment));
        commentResponse.setOwnerProfileName(user.getProfileName());
        commentResponse.setOwnerAvatar(user.getProfileAvatar());
        commentResponse.setOwnerUrl(user.getUserUrl());
        commentResponse.setNumberOfLike(0);
        commentResponse.setLikedByMe(false);

        if (!commentResponse.getReplyTo().isEmpty()){
            Comment replyTo = commentRepository.findById(commentResponse.getReplyTo()).orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_EXISTED));
            if (!replyTo.getOwnerId().equals(user.getId())){
                notificationService.sendReplyCommentNotification(user, replyTo);
            }
        } else if (!userID.equals(post.getAuthor().getId())) {
            notificationService.sendCommentNotification(user, post);
        }




        return commentResponse;
    }

    public Comment updateComment(String commentId, CommentUpdateReq data) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_EXISTED));
        commentMapper.updateComment(comment, data);
        comment.setContent(data.getContent());
        return commentRepository.save(comment);
    }

    public Set<CommentResponse> getAllComment(GetCommentReq replyCommentReq) {
        Set<Comment> comments = commentRepository.findAllByPostIdAndReplyTo(Sort.by(Sort.Direction.DESC, "creationDate"), replyCommentReq.getPostId(), replyCommentReq.getReplyTo());
        Set<CommentResponse> commentResponses = new HashSet<>();
        for (Comment comment : comments) {
            CommentResponse commentResponse = commentMapper.toCommentResponse(comment);
            User u = userRepository.findById(comment.getOwnerId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            List<LikeComment> likeCommentList = likeCommentRepository.findAllByCommentId(comment.getId());
            commentResponse.setOwnerAvatar(u.getProfileAvatar());
            commentResponse.setOwnerProfileName(u.getProfileName());
            commentResponse.setOwnerUrl(u.getUserUrl());
            commentResponse.setNumberOfLike(likeCommentList.size());
            commentResponse.setLikedByMe(likeCommentRepository.existsByCommentIdAndUserId(comment.getId(), replyCommentReq.getRootUserID()));
            commentResponses.add(commentResponse);
        }
        return commentResponses;
    }

    public String likeComment(LikeCommentRequest  request){
        if (likeCommentRepository.existsByCommentIdAndUserId(request.getCommentId(), request.getUserId())) {
            likeCommentRepository.deleteAllByCommentIdAndUserId(request.getCommentId(), request.getUserId());
            return "Comment unliked!";
        }
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Comment comment = commentRepository.findById(request.getCommentId()).orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_EXISTED));
        LikeComment likeComment = commentMapper.toLikeComment(request);
        likeComment.setLikedAt(LocalDateTime.now());
        likeCommentRepository.save(likeComment);

        notificationService.sendLikeCommentNotification(user, comment);

        return "Comment liked!";
    }
}
