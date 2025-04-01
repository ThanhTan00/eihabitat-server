package com.eihabitat.eihabitat_server.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatRoomResponse {
    String id;
    String userId;
    String userName;
    String userAvatar;
    String lastMessage;
    String lastSender;
    boolean seen;
    LocalDateTime timestamp;
}
