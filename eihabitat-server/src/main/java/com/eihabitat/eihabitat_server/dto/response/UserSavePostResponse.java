package com.eihabitat.eihabitat_server.dto.response;

import com.eihabitat.eihabitat_server.entity.UserSavePost;
import lombok.Data;

@Data
public class UserSavePostResponse {
    private String userId;
    private String postId;

    public UserSavePostResponse(UserSavePost savePost) {
        this.userId = savePost.getUserId();
        this.postId = savePost.getPostId();
    }
}

