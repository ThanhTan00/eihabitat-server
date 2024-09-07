package com.eihabitat.eihabitat_server.dto.request;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostContentReq {
    String imageId;
    String postId;
}
