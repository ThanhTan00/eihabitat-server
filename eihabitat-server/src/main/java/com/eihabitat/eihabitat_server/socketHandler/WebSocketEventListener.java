package com.eihabitat.eihabitat_server.socketHandler;

import com.eihabitat.eihabitat_server.entity.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener extends TextWebSocketHandler {
    private static final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;  // Will be injected through constructor

    // Rest of the handler implementation remains the same
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String userId = extractUserIdFromSession(session);
        userSessions.put(userId, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ChatMessage chatMessage = objectMapper.readValue(message.getPayload(), ChatMessage.class);

        WebSocketSession recipientSession = userSessions.get(chatMessage.getReceiverId());
        if (recipientSession != null && recipientSession.isOpen()) {
            recipientSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatMessage)));
        }

        saveChatMessage(chatMessage);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String userId = extractUserIdFromSession(session);
        userSessions.remove(userId);
    }

    private String extractUserIdFromSession(WebSocketSession session) {
        return session.getUri().getQuery().split("=")[1];
    }

    private void saveChatMessage(ChatMessage message) {
        // Implement saving to database
    }
}
