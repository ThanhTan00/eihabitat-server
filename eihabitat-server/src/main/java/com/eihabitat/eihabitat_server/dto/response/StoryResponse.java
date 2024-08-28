package com.eihabitat.eihabitat_server.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StoryResponse {
    private Integer id;
    String image;
    String captions;
    LocalDateTime timestamp;
    String author;
}
