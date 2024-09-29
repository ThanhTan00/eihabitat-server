//package com.eihabitat.eihabitat_server.socketHandler;
//
//import com.eihabitat.eihabitat_server.dto.response.CommentResponse;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.concurrent.CopyOnWriteArrayList;
//
//@Component
//public class CommentWebSocketHandler extends TextWebSocketHandler {
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>(); // Stores all connected sessions
//
//
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        sessions.add(session);
//    }
//
////    @Override
////    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
////        // Deserialize the incoming message to Comment object
////        Comment comment = objectMapper.readValue(message.getPayload(), Comment.class);
////
////        // Save the comment to the database
////        commentRepository.save(comment);
////
////        // Broadcast the comment to all connected clients
////        broadcastComment(comment);
////    }
//
//    public void broadcastComment(CommentResponse comment) throws Exception {
//        String commentJson = objectMapper.writeValueAsString(comment);
//
//        // Send the comment to all active WebSocket sessions
//        for (WebSocketSession session : sessions) {
//            if (session.isOpen()) {
//                session.sendMessage(new TextMessage(commentJson));
//            }
//        }
//    }
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        sessions.remove(session);
//    }
//}
