package com.eihabitat.eihabitat_server.repository;

import com.eihabitat.eihabitat_server.entity.LikeComment;
import com.eihabitat.eihabitat_server.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {
    Message findFirstByChatRoomIdOrderByTimestampDesc(String chatRoomId);
    List<Message> findTop20ByChatRoomIdOrderByTimestampDesc(String chatRoomId);
}
