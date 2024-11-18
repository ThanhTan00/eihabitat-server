package com.eihabitat.eihabitat_server.controller;

import com.eihabitat.eihabitat_server.entity.ChatMessage;
import com.eihabitat.eihabitat_server.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(ChatMessageRepository chatMessageRepository, SimpMessagingTemplate messagingTemplate) {
        this.chatMessageRepository = chatMessageRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/send")
    public ResponseEntity<ChatMessage> testSendMessage(@RequestBody ChatMessage message) {
        message.setTimestamp(LocalDateTime.now());

        // Save message to the database
        ChatMessage savedMessage = chatMessageRepository.save(message);

        // Broadcast the message to WebSocket clients
        messagingTemplate.convertAndSend("/topic/messages", savedMessage);

        return ResponseEntity.ok(savedMessage);
    }

    // Get all messages for testing
    @GetMapping("/messages")
    public ResponseEntity<List<ChatMessage>> getAllMessages() {
        return ResponseEntity.ok(chatMessageRepository.findAll());
    }

    // Get messages between two users
    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> getMessagesBetweenUsers(
            @PathVariable String senderId,
            @PathVariable String recipientId) {
        List<ChatMessage> messages = chatMessageRepository
                .findBySenderIdAndRecipientIdOrRecipientIdAndSenderIdOrderByTimestampDesc(
                        senderId, recipientId, senderId, recipientId);
        return ResponseEntity.ok(messages);
    }

    // Clear all messages (for testing)
    @DeleteMapping("/messages")
    public ResponseEntity<Void> clearAllMessages() {
        chatMessageRepository.deleteAll();
        return ResponseEntity.ok().build();
    }
}

