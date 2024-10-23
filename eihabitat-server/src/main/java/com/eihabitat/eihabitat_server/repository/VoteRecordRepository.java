package com.eihabitat.eihabitat_server.repository;

import com.eihabitat.eihabitat_server.entity.VoteRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VoteRecordRepository extends MongoRepository<VoteRecord, String> {
    List<VoteRecord> findByVoteId(String voteId);
    boolean existsByVoteIdAndUserId(String voteId, String userId);
}
