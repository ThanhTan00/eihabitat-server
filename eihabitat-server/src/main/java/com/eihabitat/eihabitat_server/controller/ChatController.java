package com.eihabitat.eihabitat_server.controller;

import com.eihabitat.eihabitat_server.entity.ChatMessage;
import com.eihabitat.eihabitat_server.entity.User;
import com.eihabitat.eihabitat_server.exception.AppException;
import com.eihabitat.eihabitat_server.exception.ErrorCode;
import com.eihabitat.eihabitat_server.repository.ChatMessageRepository;
import com.eihabitat.eihabitat_server.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    UserRepository userRepo;

    public ChatController(ChatMessageRepository chatMessageRepository, SimpMessagingTemplate messagingTemplate) {
        this.chatMessageRepository = chatMessageRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/conversations/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getConversations(@PathVariable String userId) {
        List<ChatMessage> messages = chatMessageRepository.findConversationsByUserId(userId);

        // Step 2: Extract unique participant IDs
        Set<String> participantIds = new HashSet<>();
        for (ChatMessage message : messages) {
            if (!message.getSenderId().equals(userId)) {
                participantIds.add(message.getSenderId());
            }
            if (!message.getRecipientId().equals(userId)) {
                participantIds.add(message.getRecipientId());
            }
        }

        List<Map<String, Object>> conversations = new ArrayList<>();
        for (String participantId : participantIds) {
            // Fetch user details (replace with your UserService or repository call)
            User user = userRepo.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

            // Fetch the last message between the user and this participant
            ChatMessage lastMessage = chatMessageRepository
                    .findTopBySenderIdAndRecipientIdOrRecipientIdAndSenderIdOrderByTimestampDesc(
                            userId, participantId);
            Map<String, Object> conversationData = new HashMap<>();
            conversationData.put("userId", user.getId());
            conversationData.put("userAvatar", user.getProfileAvatar());
            conversationData.put("userProfileName", user.getProfileName());
            conversationData.put("lastMessage", lastMessage != null ? lastMessage.getContent() : null);

            conversations.add(conversationData);
        }
        return ResponseEntity.ok(new ArrayList<>(conversations));
    }

    @GetMapping("/history/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> getChatHistory(
            @PathVariable String senderId, @PathVariable String recipientId) {
        List<ChatMessage> history = chatMessageRepository.findChatHistory(senderId, recipientId)
                .stream()
                .sorted(Comparator.comparing(ChatMessage::getTimestamp))
                .collect(Collectors.toList());

        return ResponseEntity.ok(history);
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

