package com.eihabitat.eihabitat_server.repository;

import com.eihabitat.eihabitat_server.dto.response.UserFollowerResponse;
import com.eihabitat.eihabitat_server.entity.UserFollow;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserFollowRepository extends MongoRepository<UserFollow, String> {
    List<UserFollow> findByFollowerId(String followerId);
    List<UserFollow> findByFollowedId(String followedId);
    Optional<UserFollow> findByFollowerIdAndFollowedId(String followerId, String followedId);
    @Query(value = "{ 'followerId': ?0 }", fields = "{ 'followedId': 1, '_id': 0 }")
    Set<String> findFollowedIdByFollowerId(String followerId);
}
