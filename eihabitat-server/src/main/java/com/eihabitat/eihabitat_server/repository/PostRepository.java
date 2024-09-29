package com.eihabitat.eihabitat_server.repository;

import com.eihabitat.eihabitat_server.entity.Post;
import com.eihabitat.eihabitat_server.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, String> {

    public Set<Post> findAllByAuthorId(String authorId);
    public Set<Post> findAllByAuthorProfileName(Sort sort, String authorId);
//
//    public List<Post> findAllPostByUserIds(@Param("users") List<String> userIds);
//
//    public List<Post> findAllPostByUserIdsSortedByDateDesc(@Param("users") List<String> userIds);


}
