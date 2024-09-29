package com.eihabitat.eihabitat_server.dto.request;

import com.eihabitat.eihabitat_server.entity.PostContent;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostCreationReq {
    String caption;
    String type;
    LocalDateTime createdAt;
    Set<PostContentReq> postContentReq;
}
