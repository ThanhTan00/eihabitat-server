package com.eihabitat.eihabitat_server.repository;

import com.eihabitat.eihabitat_server.entity.PostContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface PostContentRepository extends JpaRepository<PostContent, String> {
    Set<PostContent> findAllByPostId(String postId);
}
