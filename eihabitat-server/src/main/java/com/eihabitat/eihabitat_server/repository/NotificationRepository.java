package com.eihabitat.eihabitat_server.repository;
import com.eihabitat.eihabitat_server.entity.Notification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;


public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findTop10ByRecipientOrderByCreatedAtDesc(String recipient);
}