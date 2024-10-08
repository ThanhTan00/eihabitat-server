package com.eihabitat.eihabitat_server.repository;

import com.eihabitat.eihabitat_server.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface StoryRepository extends JpaRepository<Story, String> {
    List<Story> findByExpiresAtAfter(LocalDateTime time);
    List<Story> findByExpiresAtBefore(LocalDateTime time);
}
