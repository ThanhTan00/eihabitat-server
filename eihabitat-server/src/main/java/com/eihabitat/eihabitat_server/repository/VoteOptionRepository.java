package com.eihabitat.eihabitat_server.repository;

import com.eihabitat.eihabitat_server.entity.Option;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VoteOptionRepository extends MongoRepository<Option, String> {
    List<Option> findByVoteId(String voteId);
}
