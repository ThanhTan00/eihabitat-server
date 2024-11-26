package com.eihabitat.eihabitat_server.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String caption;
    String type;
    LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    User author;

    @Enumerated(EnumType.STRING)
    PrivacyLevel privacyLevel;
}

