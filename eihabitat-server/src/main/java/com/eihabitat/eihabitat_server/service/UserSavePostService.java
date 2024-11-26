package com.eihabitat.eihabitat_server.service;

import com.eihabitat.eihabitat_server.dto.request.UserLikePostReq;
import com.eihabitat.eihabitat_server.dto.request.UserSavePostReq;
import com.eihabitat.eihabitat_server.dto.response.UserLikePostResponse;
import com.eihabitat.eihabitat_server.dto.response.UserSavePostResponse;
import com.eihabitat.eihabitat_server.entity.UserLikePost;
import com.eihabitat.eihabitat_server.entity.UserSavePost;
import com.eihabitat.eihabitat_server.mapper.UserLikePostMapper;
import com.eihabitat.eihabitat_server.mapper.UserSavePostMapper;
import com.eihabitat.eihabitat_server.repository.UserLikePostRepository;
import com.eihabitat.eihabitat_server.repository.UserSavePostRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserSavePostService {

    private final UserSavePostRepository userSavePostRepository;
    private final UserSavePostMapper userSavePostMapper;

    public String savePost(UserSavePostReq request) {
        if (userSavePostRepository.existsByUserIdAndPostId(request.getUserId(), request.getPostId())) {
            unSavePost(request);
            return "Post unsaved successfully!";
        }
        // Use mapper to convert request DTO to entity
        UserSavePost savePost = userSavePostMapper.toUserSavePost(request);

        userSavePostRepository.save(savePost);
        return "Post saved successfully!";
    }

    public List<UserSavePostResponse> getSavedPostsForPost(String postId) {
        // Use mapper to convert entity to response DTO
        return userSavePostRepository.findByPostId(postId).stream()
                .map(userSavePostMapper::toUserSavePostResponse)
                .collect(Collectors.toList());
    }

    public void unSavePost(UserSavePostReq request) {
        userSavePostRepository.deleteUserSavePostByUserIdAndPostId(request.getUserId(), request.getPostId());
    }

    public Long getSaveCountForPost(String postId) {
        return userSavePostRepository.countByPostId(postId);
    }

    public Boolean hasUserSavedPost(String userId, String postId) {
        return userSavePostRepository.existsByUserIdAndPostId(userId, postId);
    }
}
