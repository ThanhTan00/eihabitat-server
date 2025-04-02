package com.eihabitat.eihabitat_server.service;

import com.eihabitat.eihabitat_server.dto.response.NotificationResponse;
import com.eihabitat.eihabitat_server.entity.Comment;
import com.eihabitat.eihabitat_server.entity.Notification;
import com.eihabitat.eihabitat_server.entity.Post;
import com.eihabitat.eihabitat_server.entity.User;
import com.eihabitat.eihabitat_server.enums.NotificationType;
import com.eihabitat.eihabitat_server.exception.AppException;
import com.eihabitat.eihabitat_server.exception.ErrorCode;
import com.eihabitat.eihabitat_server.mapper.NotificationMapper;
import com.eihabitat.eihabitat_server.repository.NotificationRepository;
import com.eihabitat.eihabitat_server.repository.PostContentRepository;
import com.eihabitat.eihabitat_server.repository.PostRepository;
import com.eihabitat.eihabitat_server.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class NotificationService {
    NotificationRepository notificationRepository;
    PostContentRepository postContentRepository;
    UserRepository userRepository;
    PostRepository postRepository;
    NotificationMapper notificationMapper;
    SimpMessagingTemplate messagingTemplate;

    public List<NotificationResponse> getTop10Notifications(String recipient) {
        List<Notification> notifications = notificationRepository.findTop10ByRecipientOrderByCreatedAtDesc(recipient);
        List<NotificationResponse> notificationResponses = new ArrayList<>();
        for (Notification notification : notifications) {
            User user = userRepository.findById(notification.getUserId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            NotificationResponse notificationResponse = notificationMapper.toNotificationResponse(notification);
            notificationResponse.setUserProfileName(user.getProfileName());
            notificationResponse.setUserProfileAvatar(user.getProfileAvatar());
            notificationResponse.setUserUrl(user.getUserUrl());

            if (!notification.getPostId().isEmpty()) {
                Post post = postRepository.findById(notification.getPostId()).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
                notificationResponse.setPostImage(postContentRepository.findFirstByPostId(post.getId()).getImageId());
            }
            notificationResponses.add(notificationResponse);
        }
        return notificationResponses;
    }

    public void sendLikePostNotification(User user, Post post) {
        Notification notification = Notification.builder()
                .type(NotificationType.LIKE_POST)
                .createdAt(LocalDateTime.now())
                .recipient(post.getAuthor().getProfileName())
                .postId(post.getId())
                .userId(user.getId())
                .seen(false)
                .build();

        NotificationResponse notificationResponse = notificationMapper.toNotificationResponse(notificationRepository.save(notification));
        notificationResponse.setUserProfileName(user.getProfileName());
        notificationResponse.setUserProfileAvatar(user.getProfileAvatar());
        notificationResponse.setUserUrl(user.getUserUrl());
        notificationResponse.setPostImage(postContentRepository.findFirstByPostId(post.getId()).getImageId());

        messagingTemplate.convertAndSendToUser(notificationResponse.getRecipient(), "/notifications", notificationResponse);
    }

    public void sendFollowNotification(User follower, User following) {
        Notification notification = Notification.builder()
                .type(NotificationType.FOLLOW)
                .createdAt(LocalDateTime.now())
                .recipient(following.getProfileName())
                .userId(follower.getId())
                .postId("")
                .seen(false)
                .build();
        NotificationResponse notificationResponse = notificationMapper.toNotificationResponse(notificationRepository.save(notification));
        notificationResponse.setUserProfileName(follower.getProfileName());
        notificationResponse.setUserProfileAvatar(follower.getProfileAvatar());
        notificationResponse.setUserUrl(follower.getUserUrl());

        messagingTemplate.convertAndSendToUser(notificationResponse.getRecipient(), "/notifications", notificationResponse);
    }

    public void sendCommentNotification(User user, Post post) {
        Notification notification = Notification.builder()
                .type(NotificationType.COMMENT)
                .createdAt(LocalDateTime.now())
                .recipient(post.getAuthor().getProfileName())
                .postId(post.getId())
                .userId(user.getId())
                .seen(false)
                .build();

        NotificationResponse notificationResponse = notificationMapper.toNotificationResponse(notificationRepository.save(notification));
        notificationResponse.setUserProfileName(user.getProfileName());
        notificationResponse.setUserProfileAvatar(user.getProfileAvatar());
        notificationResponse.setUserUrl(user.getUserUrl());
        notificationResponse.setPostImage(postContentRepository.findFirstByPostId(post.getId()).getImageId());

        messagingTemplate.convertAndSendToUser(notificationResponse.getRecipient(), "/notifications", notificationResponse);
    }

    public void sendLikeCommentNotification(User user, Comment comment) {
        User owner = userRepository.findById(comment.getOwnerId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Post post = postRepository.findById(comment.getPostId()).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        Notification notification = Notification.builder()
                .type(NotificationType.LIKE_COMMENT)
                .createdAt(LocalDateTime.now())
                .recipient(owner.getProfileName())
                .postId(post.getId())
                .userId(user.getId())
                .seen(false)
                .build();

        NotificationResponse notificationResponse = notificationMapper.toNotificationResponse(notificationRepository.save(notification));
        notificationResponse.setUserProfileName(user.getProfileName());
        notificationResponse.setUserProfileAvatar(user.getProfileAvatar());
        notificationResponse.setUserUrl(user.getUserUrl());
        notificationResponse.setPostImage(postContentRepository.findFirstByPostId(post.getId()).getImageId());

        messagingTemplate.convertAndSendToUser(notificationResponse.getRecipient(), "/notifications", notificationResponse);
    }

    public void sendReplyCommentNotification(User user, Comment comment) {
        User owner = userRepository.findById(comment.getOwnerId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Post post = postRepository.findById(comment.getPostId()).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        Notification notification = Notification.builder()
                .type(NotificationType.REPLY_COMMENT)
                .createdAt(LocalDateTime.now())
                .recipient(owner.getProfileName())
                .postId(post.getId())
                .userId(user.getId())
                .seen(false)
                .build();

        NotificationResponse notificationResponse = notificationMapper.toNotificationResponse(notificationRepository.save(notification));
        notificationResponse.setUserProfileName(user.getProfileName());
        notificationResponse.setUserProfileAvatar(user.getProfileAvatar());
        notificationResponse.setUserUrl(user.getUserUrl());
        notificationResponse.setPostImage(postContentRepository.findFirstByPostId(post.getId()).getImageId());

        messagingTemplate.convertAndSendToUser(notificationResponse.getRecipient(), "/notifications", notificationResponse);
    }

    public String seenNotifications(List<String> notificationIds) {
        for (String notificationId : notificationIds) {
            Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND));
            notification.setSeen(true);
            notificationRepository.save(notification);
        }
        return "Seen notifications successfully";
    }

}
