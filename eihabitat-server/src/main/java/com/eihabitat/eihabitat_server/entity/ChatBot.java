package com.eihabitat.eihabitat_server.entity;

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
@Document(collection = "chat-bot")
public class ChatBot {
    @Id
    String id;
    String userId;
    String message;
    String response;
    LocalDateTime sendAt;
    LocalDateTime receiveAt;
}

