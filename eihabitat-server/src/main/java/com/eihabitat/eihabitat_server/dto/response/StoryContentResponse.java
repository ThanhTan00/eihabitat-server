package com.eihabitat.eihabitat_server.dto.response;
import com.eihabitat.eihabitat_server.entity.Story;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StoryContentResponse {
    String id;
    Story story;
}
