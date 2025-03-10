package com.eihabitat.eihabitat_server.dto.response;

import com.eihabitat.eihabitat_server.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StoryResponse {
    String id;
    String caption;
    String imageUrl;
    LocalDateTime createdAt;
    LocalDateTime expiresAt;
    List<String> seenId;
    String authorName;
    String authorAvatar;
}
