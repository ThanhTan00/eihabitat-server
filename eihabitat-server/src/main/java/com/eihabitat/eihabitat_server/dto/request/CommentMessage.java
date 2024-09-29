package com.eihabitat.eihabitat_server.dto.request;

import lombok.Data;

@Data
public class CommentMessage {
    private String content;
    private String postId;
    private String ownerId;
}