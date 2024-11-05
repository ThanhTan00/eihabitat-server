package com.eihabitat.eihabitat_server.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserFollowerResponse {
    String id;
    String profileName;
    String profileAvatar;
    String firstName;
    String lastName;
    int followers;
    int following;
    boolean followMe=false;
    boolean followedByMe=false;
}
