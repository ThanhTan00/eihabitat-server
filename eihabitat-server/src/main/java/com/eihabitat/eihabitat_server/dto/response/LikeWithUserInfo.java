package com.eihabitat.eihabitat_server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeWithUserInfo {
    String id;
    String userId;
    String userProfileName;
    String userProfileAvatar;
    LocalDateTime likedAt;
}
