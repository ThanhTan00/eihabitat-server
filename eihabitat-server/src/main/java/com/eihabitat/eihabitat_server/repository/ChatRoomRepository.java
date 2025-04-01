package com.eihabitat.eihabitat_server.repository;

import com.eihabitat.eihabitat_server.entity.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    boolean existsBySenderIdAndRecipientId(String senderId, String recipientId);
    ChatRoom findBySenderIdAndRecipientId(String senderId, String recipientId);
    List<ChatRoom> findBySenderIdOrRecipientId(String senderId, String recipientId);
}