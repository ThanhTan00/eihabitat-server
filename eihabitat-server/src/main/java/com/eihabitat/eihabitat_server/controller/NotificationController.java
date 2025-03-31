package com.eihabitat.eihabitat_server.controller;

import com.eihabitat.eihabitat_server.dto.request.ApiResponse;
import com.eihabitat.eihabitat_server.dto.request.CommentCreationReq;
import com.eihabitat.eihabitat_server.dto.response.CommentResponse;
import com.eihabitat.eihabitat_server.dto.response.NotificationResponse;
import com.eihabitat.eihabitat_server.dto.response.StoryResponse;
import com.eihabitat.eihabitat_server.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/notification")
public class NotificationController {
    NotificationService notificationService;

    @GetMapping("/top10/{recipient}")
    public ApiResponse<List<NotificationResponse>> addComment(@PathVariable String recipient) {
        return ApiResponse.<List<NotificationResponse>>builder()
                .code(1000)
                .data(notificationService.getTop10Notifications(recipient))
                .build();
    }
}
