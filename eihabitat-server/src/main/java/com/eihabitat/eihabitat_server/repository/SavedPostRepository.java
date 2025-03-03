package com.eihabitat.eihabitat_server.repository;

import com.eihabitat.eihabitat_server.entity.SavedPost;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Set;

public interface SavedPostRepository extends MongoRepository<SavedPost, String> {
    List<String> findAllPostIdsByAlbumId (String albumId);
    boolean existsByPostIdAndUserId (String postId, String userId);
    void deleteByPostIdAndUserId (String postId, String userId);
    List<SavedPost> findTop4ByUserId(String userId);
    List<SavedPost> findAllByUserId(Sort sort, String userId);
}

