package com.eihabitat.eihabitat_server.dto.response;

import com.eihabitat.eihabitat_server.entity.PrivacyLevel;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostOnPersonalWallResponse {
    String id;
    LocalDateTime createdAt;
    String representImage;
    int numberOfLikes;
    int numberOfComments;
    PrivacyLevel privacyLevel;
}
