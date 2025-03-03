package com.eihabitat.eihabitat_server.dto.response;

import com.eihabitat.eihabitat_server.entity.PostContent;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AlbumResponse {
    List<PostContent> representImages;
}
