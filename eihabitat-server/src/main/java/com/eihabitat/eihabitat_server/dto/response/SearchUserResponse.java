package com.eihabitat.eihabitat_server.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchUserResponse {
    String id;
    String firstName;
    String lastName;
    String profileName;
    String profileAvatar;
    String userUrl;
    int followers;
}
