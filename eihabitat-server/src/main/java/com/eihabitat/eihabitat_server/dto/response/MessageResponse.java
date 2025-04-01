package com.eihabitat.eihabitat_server.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageResponse {
    String id;
    String senderId;
    String content;
    LocalDateTime timestamp;
    boolean seen;
    String senderAvatar;
    String senderUrl;
}
