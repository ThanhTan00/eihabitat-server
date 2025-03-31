package com.eihabitat.eihabitat_server.repository;

import com.eihabitat.eihabitat_server.entity.LikeComment;
import com.eihabitat.eihabitat_server.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findTop10ByRecipientOrderByCreatedAtDesc(String recipient);
}