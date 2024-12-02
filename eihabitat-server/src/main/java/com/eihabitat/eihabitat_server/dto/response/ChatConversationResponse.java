package com.eihabitat.eihabitat_server.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatConversationResponse {
    String id;
    String userProfileName;
    String userAvatar;
    String lastMessage;
}
