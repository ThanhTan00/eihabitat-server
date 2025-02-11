package com.eihabitat.eihabitat_server.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentCreationReq {
    @NotBlank(message = "Required content")
    @Length(min = 1, message = "Content minimum is 1 character")
    String content;
    String postId;
    String replyTo;
}
