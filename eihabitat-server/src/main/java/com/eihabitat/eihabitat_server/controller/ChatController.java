package com.eihabitat.eihabitat_server.controller;

import com.eihabitat.eihabitat_server.dto.request.ApiResponse;
import com.eihabitat.eihabitat_server.dto.request.ChatBotMessageReq;
import com.eihabitat.eihabitat_server.dto.request.MessageReq;
import com.eihabitat.eihabitat_server.dto.response.*;
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
@RequestMapping("/chat")
public class ChatController {
    ChatService chatService;

    @GetMapping("/chatRoom/{userId}")
    public ApiResponse<List<ChatRoomResponse>> getAllRooms(@PathVariable String userId) {
        return  ApiResponse.<List<ChatRoomResponse>>builder()
                .code(1000)
                .data(chatService.getAllChatRooms(userId))
                .build();
    }

    @GetMapping("/history/{roomId}")
    public ApiResponse<List<MessageResponse>> getChatHistory(@PathVariable String roomId) {
        return  ApiResponse.<List<MessageResponse>>builder()
                .code(1000)
                .data(chatService.getMessageHistory(roomId))
                .build();
    }
//
    @PostMapping("/send")
    public ApiResponse<MessageResponse> testSendMessage(@RequestBody MessageReq message) {
        return  ApiResponse.<MessageResponse>builder()
                .code(1000)
                .data(chatService.sendMessage(message))
                .build();
    }
//
//    @GetMapping("/messages")
//    public ResponseEntity<List<ChatMessage>> getAllMessages() {
//        return ResponseEntity.ok(chatService.getAllMessages());
//    }
//
//    @GetMapping("/messages/{senderId}/{recipientId}")
//    public ResponseEntity<List<ChatMessage>> getMessagesBetweenUsers(
//            @PathVariable String senderId, @PathVariable String recipientId) {
//        return ResponseEntity.ok(chatService.getMessagesBetweenUsers(senderId, recipientId));
//    }
//
//    @DeleteMapping("/messages")
//    public ResponseEntity<Void> clearAllMessages() {
//        chatService.clearAllMessages();
//        return ResponseEntity.ok().build();
//    }

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

