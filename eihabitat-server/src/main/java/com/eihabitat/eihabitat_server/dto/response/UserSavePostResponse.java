package com.eihabitat.eihabitat_server.dto.response;

import com.eihabitat.eihabitat_server.entity.UserSavePost;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserSavePostResponse {
    private String userId;
    private String postId;
    private LocalDateTime saveAt; // Include the date and time in the response

    // Constructor
    public UserSavePostResponse(String userId, String postId, LocalDateTime dateTime) {
        this.userId = userId;
        this.postId = postId;
        this.saveAt = dateTime;
    }
}

