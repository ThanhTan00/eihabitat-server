package com.eihabitat.eihabitat_server.entity;

//import com.eihabitat.eihabitat_server.service.StoryListener;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
//@EntityListeners(StoryListener.class)
public class Story {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String type;
    LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    User author;

//    @Transient
//    private boolean shouldBeRemoved = false;
}
