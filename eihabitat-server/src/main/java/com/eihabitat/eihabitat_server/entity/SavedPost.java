package com.eihabitat.eihabitat_server.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "savedPost")
public class SavedPost {
    @Id
    String id;
    String postId;
    String userId;
    String albumId;
    LocalDateTime savedAt;
}
