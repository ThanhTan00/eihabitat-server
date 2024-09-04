package com.eihabitat.eihabitat_server.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StoryCreationReq {

//    @NotBlank(message = "IMAGE_REQUIRED")
//    String image;
//
//    @Size(max = 200, message = "CAPTION_TOO_LONG")
//    String captions;
//
//    @NotBlank(message = "AUTHOR_REQUIRED")
//    String author;

    String type;
    LocalDateTime createdAt;
    String author;
}
