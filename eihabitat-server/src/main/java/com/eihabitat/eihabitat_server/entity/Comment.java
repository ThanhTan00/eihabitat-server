package com.eihabitat.eihabitat_server.entity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    String content;
    String ownerUsername;
    String ownerDisplayName;
    Instant creationDate;

    @Column(name = "a_post_id")
    String postId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    Post post;
}
