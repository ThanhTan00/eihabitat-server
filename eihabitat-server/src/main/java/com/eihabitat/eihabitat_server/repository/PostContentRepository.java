package com.eihabitat.eihabitat_server.repository;

import com.eihabitat.eihabitat_server.entity.PostContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostContentRepository extends JpaRepository<PostContent, String> {
}
