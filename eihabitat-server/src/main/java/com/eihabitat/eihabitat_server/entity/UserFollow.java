package com.eihabitat.eihabitat_server.entity;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "follows")
public class UserFollow {
    @Id
    private String id;

    private String followerId;
    private String followedId;
    Date followedAt;
}
