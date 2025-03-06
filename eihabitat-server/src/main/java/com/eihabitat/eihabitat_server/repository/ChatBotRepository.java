package com.eihabitat.eihabitat_server.repository;

import com.eihabitat.eihabitat_server.entity.ChatBot;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatBotRepository extends MongoRepository<ChatBot, String> {
    List<ChatBot> findAllByUserId(Sort sort, String userId);
}
