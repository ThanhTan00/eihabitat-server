package com.eihabitat.eihabitat_server.service;

import com.eihabitat.eihabitat_server.entity.Notification;
import com.eihabitat.eihabitat_server.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public Notification createNotification(String recipientId, String senderId, String type, String targetId) {
        // Create a new notification
        Notification notification = new Notification();
        notification.setId(UUID.randomUUID().toString());
        notification.setRecipientId(recipientId);
        notification.setSenderId(senderId);
        notification.setType(type);
        notification.setTargetId(targetId);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);

        // Save to MongoDB
        Notification savedNotification = notificationRepository.save(notification);

        // Broadcast notification via WebSocket
        messagingTemplate.convertAndSend("/topic/notifications/" + recipientId, savedNotification);

        return savedNotification;
    }

    public List<Notification> getNotifications(String recipientId) {
        return notificationRepository.findByRecipientIdOrderByCreatedAtDesc(recipientId);
    }

    public void markAsRead(String notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setRead(true);
        notificationRepository.save(notification);
    }
}

