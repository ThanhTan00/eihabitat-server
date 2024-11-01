package com.eihabitat.eihabitat_server.entity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class PostContent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String imageId;
    String postId;
    private String s3Url;
}
