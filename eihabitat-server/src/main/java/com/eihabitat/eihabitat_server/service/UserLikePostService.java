package com.eihabitat.eihabitat_server.service;

import com.eihabitat.eihabitat_server.dto.request.UserLikePostReq;
import com.eihabitat.eihabitat_server.dto.response.UserLikePostResponse;
import com.eihabitat.eihabitat_server.entity.UserLikePost;
import com.eihabitat.eihabitat_server.mapper.UserLikePostMapper;
import com.eihabitat.eihabitat_server.repository.UserLikePostRepository;
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
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserLikePostService {
    UserLikePostRepository userLikePostRepository;
    UserLikePostMapper userLikePostMapper;

    public String likePost(UserLikePostReq request) {
        if (userLikePostRepository.existsByUserIdAndPostId(request.getUserId(), request.getPostId())) {
            unlikePost(request);
            return "Post unliked successfully!";
        }

        UserLikePost userLikePost = userLikePostMapper.toUserLikePost(request);
        userLikePost.setLikedAt(LocalDateTime.now());

        userLikePostRepository.save(userLikePost);

        return "Post liked successfully!";
    }

    public void unlikePost(UserLikePostReq request) {
        userLikePostRepository.deleteByUserIdAndPostId(request.getUserId(), request.getPostId());
    }

    public List<UserLikePostResponse> getLikesForPost(String postId) {
        List<UserLikePost> likes = userLikePostRepository.findByPostId(postId);
        return likes.stream()
                .map(userLikePostMapper::toUserLikePostResponse)
                .collect(Collectors.toList());
    }

    public long getLikeCountForPost(String postId) {
        return userLikePostRepository.countByPostId(postId);
    }

    public boolean hasUserLikedPost(String userId, String postId) {
        return userLikePostRepository.existsByUserIdAndPostId(userId, postId);
    }
}
