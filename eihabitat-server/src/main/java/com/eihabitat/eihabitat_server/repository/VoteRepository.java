package com.eihabitat.eihabitat_server.repository;

import com.eihabitat.eihabitat_server.entity.Vote;
import com.eihabitat.eihabitat_server.entity.VoteRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface VoteRepository extends MongoRepository<Vote, String> {
}

