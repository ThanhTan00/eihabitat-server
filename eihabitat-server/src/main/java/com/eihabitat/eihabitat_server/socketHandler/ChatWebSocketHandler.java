package com.eihabitat.eihabitat_server.socketHandler;

import com.eihabitat.eihabitat_server.entity.ChatMessage;
import com.eihabitat.eihabitat_server.entity.DeliveryStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {
    private static final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    private String extractUserIdFromSession(WebSocketSession session) {
        try {
            // Extract userId from URI query parameter
            String query = session.getUri().getQuery();
            if (query != null) {
                String[] params = query.split("&");
                for (String param : params) {
                    if (param.startsWith("userId=")) {
                        return param.substring("userId=".length());
                    }
                }
            }

            // Fallback: Extract from URI path or session attributes if needed
            String path = session.getUri().getPath();
            if (path != null && path.contains("/")) {
                String[] pathSegments = path.split("/");
                return pathSegments[pathSegments.length - 1];
            }

            // Last resort: Generate a unique identifier
            return UUID.randomUUID().toString();
        } catch (Exception e) {
            log.error("Error extracting user ID from session", e);
            return UUID.randomUUID().toString();
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String userId = extractUserIdFromSession(session);
        userSessions.put(userId, session);
        log.info("User {} connected. Total active sessions: {}", userId, userSessions.size());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ChatMessage chatMessage = objectMapper.readValue(message.getPayload(), ChatMessage.class);
        log.info("Message received from {} to {}: {}", chatMessage.getSenderId(), chatMessage.getRecipientId(), chatMessage.getContent()); // Send to recipient
        WebSocketSession recipientSession = userSessions.get(chatMessage.getRecipientId());
        if (recipientSession != null && recipientSession.isOpen()) {
            recipientSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatMessage)));
            log.info("Message delivered to user {}", chatMessage.getRecipientId());
        } else {
            log.warn("Recipient {} is not connected", chatMessage.getRecipientId());
        } // Send delivery confirmation to sender
        WebSocketSession senderSession = userSessions.get(chatMessage.getSenderId());
        if (senderSession != null && senderSession.isOpen()) {
            DeliveryStatus status = new DeliveryStatus(chatMessage.getId(), "DELIVERED");
            senderSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(status)));
            log.info("Delivery confirmation sent to user {}", chatMessage.getSenderId());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String userId = extractUserIdFromSession(session);
        userSessions.remove(userId);
        log.info("User {} disconnected. Remaining active sessions: {}", userId, userSessions.size());
    }
}