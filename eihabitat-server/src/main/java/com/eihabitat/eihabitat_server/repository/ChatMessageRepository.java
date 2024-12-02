package com.eihabitat.eihabitat_server.repository;

import com.eihabitat.eihabitat_server.entity.ChatMessage;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findBySenderIdAndRecipientIdOrRecipientIdAndSenderIdOrderByTimestampDesc(
            String senderId, String receiverId, String receiverId2, String senderId2);

    @Query("{'$or': [{'senderId': ?0}, {'recipientId': ?0}]}")
    List<ChatMessage> findConversationsByUserId(String userId);

    // Find chat history between two users
    @Query("{$or: [ {'senderId': ?0, 'recipientId': ?1}, {'senderId': ?1, 'recipientId': ?0} ] }")
    List<ChatMessage> findChatHistory(String senderId, String recipientId);

    @Query("{$or: [ {'senderId': ?0, 'recipientId': ?1}, {'senderId': ?1, 'recipientId': ?0} ] }")
    List<ChatMessage> findMessagesBySenderIdAndRecipientIdOrRecipientIdAndSenderId(
            String senderId, String recipientId, Sort sort);

}
