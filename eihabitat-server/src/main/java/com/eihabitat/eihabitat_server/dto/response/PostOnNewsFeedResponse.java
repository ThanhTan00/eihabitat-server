package com.eihabitat.eihabitat_server.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostOnNewsFeedResponse {
    String id;
    String caption;
    String type;
    LocalDateTime createdAt;
    String authorProfileName;
    String authorProfileAvatar;
    Set<PostContentResponse> postContentSet;
    int numberOfLikes;
    int numberOfComments;
    String latestUserLike;
    String latestUserLikeAvatar;
}
