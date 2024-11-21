package com.eihabitat.eihabitat_server.entity;



import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "save-post")
public class UserSavePost {

    @Id
    String id;

    String userId;
    String postId;
    LocalDateTime saveAt;

}

