package com.eihabitat.eihabitat_server.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserFollowResponse {
    String followerId;
    String followedId;
    Date followedAt;
}
