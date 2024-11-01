package com.eihabitat.eihabitat_server.dto.request;

import com.eihabitat.eihabitat_server.entity.PostContent;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
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
    List<MultipartFile> images;
}
