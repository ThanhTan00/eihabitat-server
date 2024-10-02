package com.eihabitat.eihabitat_server.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserFollowReq {
    private String followerId;
    private String followedId;
    String followerProfileName;
    String followedProfileName;
}
