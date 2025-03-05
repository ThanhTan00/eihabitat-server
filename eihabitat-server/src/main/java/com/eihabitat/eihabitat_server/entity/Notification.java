package com.eihabitat.eihabitat_server.entity;

import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "notifications")
public class Notification {
    @Id
    private String id;

    private String recipientId; // User who will receive the notification
    private String senderId; // User who triggered the notification
    private String type; // COMMENT, LIKE, FOLLOW
    private String targetId; // Post ID, Comment ID, etc.
    private LocalDateTime createdAt;
    private boolean isRead;
}

