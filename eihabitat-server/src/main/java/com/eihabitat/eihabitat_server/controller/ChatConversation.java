package com.eihabitat.eihabitat_server.controller;

import lombok.Data;

import java.time.LocalDateTime;

// Data Transfer Object for conversation list
@Data
public class ChatConversation {
    private String userId;
    private String username;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private int unreadCount;
}
