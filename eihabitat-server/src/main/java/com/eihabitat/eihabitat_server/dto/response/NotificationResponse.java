package com.eihabitat.eihabitat_server.dto.response;

import com.eihabitat.eihabitat_server.enums.NotificationType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationResponse {
    String id;
    NotificationType type;
    String recipient;
    LocalDateTime createdAt;
    String userProfileName;
    String userProfileAvatar;
    String userUrl;
    String postId;
    String postImage;
}
