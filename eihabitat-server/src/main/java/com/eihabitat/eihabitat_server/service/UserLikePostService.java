package com.eihabitat.eihabitat_server.service;

import com.eihabitat.eihabitat_server.dto.request.UserLikePostReq;
import com.eihabitat.eihabitat_server.dto.response.NotificationResponse;
import com.eihabitat.eihabitat_server.dto.response.UserLikePostResponse;
import com.eihabitat.eihabitat_server.entity.Notification;
import com.eihabitat.eihabitat_server.entity.Post;
import com.eihabitat.eihabitat_server.entity.User;
import com.eihabitat.eihabitat_server.entity.UserLikePost;
import com.eihabitat.eihabitat_server.enums.NotificationType;
import com.eihabitat.eihabitat_server.exception.AppException;
import com.eihabitat.eihabitat_server.exception.ErrorCode;
import com.eihabitat.eihabitat_server.mapper.NotificationMapper;
import com.eihabitat.eihabitat_server.mapper.UserLikePostMapper;
import com.eihabitat.eihabitat_server.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserLikePostService {
    UserLikePostRepository userLikePostRepository;
    PostRepository postRepository;
    UserRepository userRepository;
    UserLikePostMapper userLikePostMapper;
    NotificationService notificationService;

    public String likePost(UserLikePostReq request) {
        if (userLikePostRepository.existsByUserIdAndPostId(request.getUserId(), request.getPostId())) {
            unlikePost(request);
            return "Post unliked successfully!";
        }
        UserLikePost userLikePost = userLikePostMapper.toUserLikePost(request);
        userLikePost.setLikedAt(LocalDateTime.now());
        userLikePostRepository.save(userLikePost);


        Post post = postRepository.findById(request.getPostId()).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        if (!request.getUserId().equals(post.getAuthor().getId())) {
            User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            notificationService.sendLikePostNotification(user, post);
        }

        return "Post liked successfully!";
    }

    public void unlikePost(UserLikePostReq request) {
        userLikePostRepository.deleteByUserIdAndPostId(request.getUserId(), request.getPostId());
    }

    public List<UserLikePostResponse> getLikesForPost(String postId) {
        List<UserLikePost> likes = userLikePostRepository.findByPostId(postId);
        return likes.stream()
                .map(userLikePostMapper::toUserLikePostResponse)
                .collect(Collectors.toList());
    }

    public long getLikeCountForPost(String postId) {
        return userLikePostRepository.countByPostId(postId);
    }

    public boolean hasUserLikedPost(String userId, String postId) {
        return userLikePostRepository.existsByUserIdAndPostId(userId, postId);
    }
}
