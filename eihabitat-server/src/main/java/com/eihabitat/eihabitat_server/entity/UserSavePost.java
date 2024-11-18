package com.eihabitat.eihabitat_server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_save_post")
public class UserSavePost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Primary key with auto-generated value

    private String userId;
    private String postId;

    // Constructor without primary key (id)
    public UserSavePost(String userId, String postId) {
        this.userId = userId;
        this.postId = postId;
    }
}

