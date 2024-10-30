package com.eihabitat.eihabitat_server.repository;

import com.eihabitat.eihabitat_server.entity.Vote;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VoteRepository extends MongoRepository<Vote, String> {
}

