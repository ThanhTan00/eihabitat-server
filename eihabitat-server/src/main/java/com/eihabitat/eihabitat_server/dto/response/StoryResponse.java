package com.eihabitat.eihabitat_server.dto.response;

import com.eihabitat.eihabitat_server.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StoryResponse {
    String id;
    Set<StoryContentResponse> storyContentSet;
    LocalDateTime createdAt;
    LocalDateTime expiredAt;
    String authorAvatar;
    String authorProfileName;
}
