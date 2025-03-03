package com.eihabitat.eihabitat_server.repository;

import com.eihabitat.eihabitat_server.entity.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Set;

public interface CommentRepository extends MongoRepository<Comment, String> {
    Set<Comment> findAllByPostIdAndReplyTo(Sort sort, String postId, String replyTo);
    List<Comment> findAllByPostId(String postID);
}
