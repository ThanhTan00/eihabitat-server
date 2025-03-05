package com.eihabitat.eihabitat_server.controller;

import com.eihabitat.eihabitat_server.entity.Notification;
import com.eihabitat.eihabitat_server.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/{recipientId}")
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable String recipientId) {
        return ResponseEntity.ok(notificationService.getNotifications(recipientId));
    }

    @PostMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable String notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok().build();
    }
}

