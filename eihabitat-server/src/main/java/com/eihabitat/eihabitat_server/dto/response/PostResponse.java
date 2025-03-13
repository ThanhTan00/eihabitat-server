package com.eihabitat.eihabitat_server.dto.response;

import com.eihabitat.eihabitat_server.entity.PostContent;
import com.eihabitat.eihabitat_server.entity.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostResponse {
    String id;
    String caption;
    String type;
    LocalDateTime createdAt;
    String authorId;
    String authorProfileName;
    String authorProfileAvatar;
    String authorUrl;
    boolean story;
    boolean newStory;
    Set<PostContentResponse> postContentSet;
    int numberOfComments;
    int numberOfLikes;
    String latestUserLike;
    String latestUserLikeAvatar;
    boolean isLikeByUser;
    boolean isSavedByUser;
}
