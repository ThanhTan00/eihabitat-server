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
@Document(collection = "comments")
public class Comment {
    @Id
    String id;
    String content;
    LocalDateTime creationDate;
    String ownerId;
    String postId;
    String replyTo;
}
