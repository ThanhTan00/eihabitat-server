package com.eihabitat.eihabitat_server.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserLikePostResponse {
    String id;
    String userId;
    String postId;
    Long totalLikes;
    LocalDateTime likedAt;
}
