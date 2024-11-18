package com.eihabitat.eihabitat_server.repository;

import com.eihabitat.eihabitat_server.entity.UserLikePost;
import com.eihabitat.eihabitat_server.entity.UserSavePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSavePostRepository extends JpaRepository<UserSavePost, Long> {
    List<UserSavePost> findByPostId(String postId);
    Long countByPostId(String postId);
    Boolean existsByUserIdAndPostId(String userId, String postId);
}

