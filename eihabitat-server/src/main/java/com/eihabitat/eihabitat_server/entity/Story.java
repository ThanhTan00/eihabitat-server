package com.eihabitat.eihabitat_server.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "stories")
public class Story {
    @Id
    String id;
    String caption;
    String imageUrl;
    LocalDateTime createdAt;
    LocalDateTime expiresAt;
    List<String> seenId;

    String authorId;
}
