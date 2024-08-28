package com.eihabitat.eihabitat_server.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

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
    String author;
}
