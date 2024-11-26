package com.eihabitat.eihabitat_server.repository;

import com.eihabitat.eihabitat_server.entity.StoryContent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StoryContentRepo extends MongoRepository<StoryContent, String> {
    List<StoryContent> findByStoryId(String storyId);
}
