package com.eihabitat.eihabitat_server.dto.response;

import com.eihabitat.eihabitat_server.entity.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

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
    User author;
}
