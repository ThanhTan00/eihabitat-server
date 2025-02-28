package com.eihabitat.eihabitat_server.repository;

import com.eihabitat.eihabitat_server.entity.Album;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AlbumRepository extends MongoRepository<Album, String> {
    boolean existsById(String id);
    List<Album> findAllByUserId(String userId);
}
