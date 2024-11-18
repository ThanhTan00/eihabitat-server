package com.eihabitat.eihabitat_server.controller;

import com.eihabitat.eihabitat_server.entity.Message;
import com.eihabitat.eihabitat_server.repository.MessageRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/app")

public class WebSocketController {
    private SimpMessagingTemplate messagingTemplate;

    private MessageRepository messageRepository;

    @PostMapping ("/message")
    public void sendMessage(@RequestBody Message message) {
        message.setTimestamp(LocalDateTime.now());
        messageRepository.save(message); // Save message to database

        // Send the message to the recipient
        messagingTemplate.convertAndSendToUser(
                message.getRecipient(),
                "/queue/messages",
                message
        );
    }
}
