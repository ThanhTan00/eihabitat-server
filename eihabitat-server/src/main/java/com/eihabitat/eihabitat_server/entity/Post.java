package com.eihabitat.eihabitat_server.entity;

import com.eihabitat.eihabitat_server.dto.response.UserResponse;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
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
}

