package com.eihabitat.eihabitat_server.controller;

import com.eihabitat.eihabitat_server.dto.request.ApiResponse;
import com.eihabitat.eihabitat_server.dto.request.ChatBotMessageReq;
import com.eihabitat.eihabitat_server.dto.response.AlbumResponse;
import com.eihabitat.eihabitat_server.dto.response.ChatBotMessageResponse;
import com.eihabitat.eihabitat_server.dto.response.ChatConversationResponse;
import com.eihabitat.eihabitat_server.entity.ChatBot;
import com.eihabitat.eihabitat_server.entity.ChatMessage;
import com.eihabitat.eihabitat_server.service.ChatService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/chat")
public class ChatController {
    ChatService chatService;

    @GetMapping("/conversations/{userId}")
    public ResponseEntity<List<ChatConversationResponse>> getConversations(@PathVariable String userId) {
        return ResponseEntity.ok(chatService.getConversations(userId));
    }

    @GetMapping("/history/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> getChatHistory(
            @PathVariable String senderId, @PathVariable String recipientId) {
        return ResponseEntity.ok(chatService.getChatHistory(senderId, recipientId));
    }

    @PostMapping("/send")
    public ResponseEntity<ChatMessage> testSendMessage(@RequestBody ChatMessage message) {
        return ResponseEntity.ok(chatService.sendMessage(message));
    }

    @GetMapping("/messages")
    public ResponseEntity<List<ChatMessage>> getAllMessages() {
        return ResponseEntity.ok(chatService.getAllMessages());
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> getMessagesBetweenUsers(
            @PathVariable String senderId, @PathVariable String recipientId) {
        return ResponseEntity.ok(chatService.getMessagesBetweenUsers(senderId, recipientId));
    }

    @DeleteMapping("/messages")
    public ResponseEntity<Void> clearAllMessages() {
        chatService.clearAllMessages();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/chatBot/{userId}")
    public ApiResponse<ChatBot> chatBot(@PathVariable String userId, @RequestBody ChatBotMessageReq req) {
        ChatBot result = chatService.chatWithBot(userId, req);
        return ApiResponse.<ChatBot>builder()
                .code(1000)
                .data(result)
                .build();
    }

    @GetMapping("/chatBot/{userId}")
    public ApiResponse<List<ChatBot>> getAllChatBotHistory(@PathVariable String userId) {
        List<ChatBot> result = chatService.getChatBotHistory(userId);
        return ApiResponse.<List<ChatBot>>builder()
                .code(1000)
                .data(result)
                .build();
    }
}

