package com.eihabitat.eihabitat_server.repository;

import com.eihabitat.eihabitat_server.entity.SavedPost;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Set;

public interface SavedPostRepository extends MongoRepository<SavedPost, String> {
    List<String> findAllPostIdsByAlbumId (String albumId);
}

