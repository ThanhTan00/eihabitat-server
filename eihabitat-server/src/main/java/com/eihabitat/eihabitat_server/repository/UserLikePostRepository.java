package com.eihabitat.eihabitat_server.repository;

import com.eihabitat.eihabitat_server.entity.UserLikePost;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserLikePostRepository extends MongoRepository <UserLikePost, String> {
    // Find if a user has liked a specific post
    Optional<UserLikePost> findByUserIdAndPostId(String userId, String postId);

    // Count likes for a specific post
    long countByPostId(String postId);

    // Find all likes for a specific post
    List<UserLikePost> findByPostId(String postId);

    // Delete a like by userId and postId
    void deleteByUserIdAndPostId(String userId, String postId);

    // Custom query to check if a user has liked a post
    boolean existsByUserIdAndPostId(String userId, String postId);
}
