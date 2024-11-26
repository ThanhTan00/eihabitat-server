package com.eihabitat.eihabitat_server.entity;

import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "story-contents")
public class StoryContent {
    @Id
    String id;
    String imageId;
    String storyId;
}
