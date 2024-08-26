package com.eihabitat.eihabitat_server.repository;

import com.eihabitat.eihabitat_server.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, String> {
    public List<Post> findByUserId(String userId);

    public List<Post> findAllPostByUserIds(@Param("users") List<String> userIds);

    public List<Post> findAllPostByUserIdsSortedByDateDesc(@Param("users") List<String> userIds);
}
