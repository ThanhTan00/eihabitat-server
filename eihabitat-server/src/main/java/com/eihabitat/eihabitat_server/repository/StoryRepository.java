package com.eihabitat.eihabitat_server.repository;

import com.eihabitat.eihabitat_server.entity.Story;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface StoryRepository extends MongoRepository<Story, String> {
    List<Story> findByExpiresAtAfter(LocalDateTime time);
    List<Story> findByExpiresAtBefore(LocalDateTime time);
    List<Story> findAllByAuthorIdAndExpiresAtAfter(Sort sort, String authorId, LocalDateTime time);
    boolean existsByAuthorIdAndExpiresAtAfter(String authorId, LocalDateTime time);
    Story getFirstByAuthorIdAndExpiresAtAfter(String authorId, LocalDateTime time);
    Story getLastByAuthorIdAndExpiresAtAfter(String authorId, LocalDateTime time);
}
