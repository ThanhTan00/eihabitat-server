package com.eihabitat.eihabitat_server.repository;

import com.eihabitat.eihabitat_server.dto.response.UserFollowResponse;
import com.eihabitat.eihabitat_server.entity.UserFollow;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface UserFollowRepository extends MongoRepository<UserFollow, String> {
    List<UserFollow> findByFollowerId(String followerId);
    List<UserFollow> findByFollowedId(String followedId);
    Optional<UserFollow> findByFollowerIdAndFollowedId(String followerId, String followedId);
    UserFollowResponse deleteByFollowerIdAndFollowedId(String followerId, String followedId);
}
