package com.eihabitat.eihabitat_server.repository;

import com.eihabitat.eihabitat_server.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoryRepository extends JpaRepository<Story, String> {
    public Story createStory(Story story,String userId);

    public List<Story> findStoryByUserId(String userId);
}
