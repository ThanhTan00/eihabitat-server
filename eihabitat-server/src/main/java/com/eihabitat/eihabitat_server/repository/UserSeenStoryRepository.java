package com.eihabitat.eihabitat_server.repository;

import com.eihabitat.eihabitat_server.entity.UserSeenStory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserSeenStoryRepository extends MongoRepository<UserSeenStory, String> {
    boolean existsByUserIdAndStoryId(String userId, String storyId);
}
