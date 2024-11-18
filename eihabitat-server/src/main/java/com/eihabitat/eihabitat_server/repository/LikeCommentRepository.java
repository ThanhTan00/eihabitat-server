package com.eihabitat.eihabitat_server.repository;

import com.eihabitat.eihabitat_server.entity.LikeComment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LikeCommentRepository extends MongoRepository<LikeComment, String> {
    List<LikeComment> findAllByCommentId(String commentId);
    boolean existsByCommentIdAndUserId(String commentId,String userId);
    void deleteAllByCommentIdAndUserId(String commentId, String userId);
}
