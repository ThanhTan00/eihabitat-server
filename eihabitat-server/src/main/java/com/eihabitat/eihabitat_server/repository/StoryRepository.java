package com.eihabitat.eihabitat_server.repository;

import com.eihabitat.eihabitat_server.entity.Story;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface StoryRepository extends MongoRepository<Story, String> {
    List<Story> findByExpiresAtAfter(LocalDateTime time);
    List<Story> findByExpiresAtBefore(LocalDateTime time);
    List<Story> findAllByAuthorIdAndExpiresAtAfter(String authorId, LocalDateTime time);
}
