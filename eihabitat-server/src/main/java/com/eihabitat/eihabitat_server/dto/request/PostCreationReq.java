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
//    @NotBlank(message = "CAPTION_REQUIRED")
//    @Size(max = 500, message = "CAPTION_TOO_LONG")
//    String caption;
//
//    @NotBlank(message = "IMAGE_REQUIRED")
//    String image;
//
//    @Size(max = 100, message = "LOCATION_TOO_LONG")
//    String location;
    String caption;
    String type;
    LocalDateTime createdAt;
    Set<PostContentReq> postContentReq;
}
