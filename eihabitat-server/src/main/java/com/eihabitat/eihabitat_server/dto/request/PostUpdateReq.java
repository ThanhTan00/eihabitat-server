package com.eihabitat.eihabitat_server.dto.request;

import com.eihabitat.eihabitat_server.entity.PrivacyLevel;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostUpdateReq {
    String caption;
    String type;
    LocalDateTime updatedAt;
    String author;
    PrivacyLevel privacyLevel;
}
