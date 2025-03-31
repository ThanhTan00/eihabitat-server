package com.eihabitat.eihabitat_server.entity;

import com.eihabitat.eihabitat_server.enums.NotificationType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "notification")
public class Notification {
    @Id
    String id;
    NotificationType type;
    String recipient;
    LocalDateTime createdAt;
    String userId;
    String postId;
}
