package com.eihabitat.eihabitat_server.dto.response;

import com.eihabitat.eihabitat_server.entity.User;
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
    private Long id;
    private Long userId;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
}
