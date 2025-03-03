package com.eihabitat.eihabitat_server.service;

import com.eihabitat.eihabitat_server.dto.request.SavePostReq;
import com.eihabitat.eihabitat_server.dto.response.AlbumResponse;
import com.eihabitat.eihabitat_server.dto.response.PostOnPersonalWallResponse;
import com.eihabitat.eihabitat_server.dto.response.UserLikePostResponse;
import com.eihabitat.eihabitat_server.entity.PostContent;
import com.eihabitat.eihabitat_server.entity.SavedPost;
import com.eihabitat.eihabitat_server.exception.AppException;
import com.eihabitat.eihabitat_server.exception.ErrorCode;
import com.eihabitat.eihabitat_server.mapper.PostMapper;
import com.eihabitat.eihabitat_server.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SavedPostService {
    SavedPostRepository savedPostRepository;
    PostContentRepository postContentRepository;
    UserLikePostRepository likePostRepository;
    CommentRepository commentRepository;
    PostMapper postMapper;

    public String savePost(SavePostReq req) {
        if (savedPostRepository.existsByPostIdAndUserId(req.getPostId(), req.getUserId())) {
            savedPostRepository.deleteByPostIdAndUserId(req.getPostId(), req.getUserId());
            return "Unsaved post successfully!";
        } else {
            SavedPost savedPost = postMapper.toSavedPost(req);
            savedPost.setSavedAt(LocalDateTime.now());
            savedPostRepository.save(savedPost);
            return "Saved post successfully!";
        }
    }

    public AlbumResponse getTop4(String userId) {
        List<SavedPost> top4 =  savedPostRepository.findTop4ByUserId(Sort.by(Sort.Direction.DESC, "savedAt"), userId);
        AlbumResponse alResponse = new AlbumResponse();
        List<PostContent> represents = new ArrayList<>();
        for (SavedPost savedPost : top4) {
            represents.add(postContentRepository.findFirstByPostId(savedPost.getPostId()));
        }
        alResponse.setRepresentImages(represents);
        return alResponse;
    }

    public List<PostOnPersonalWallResponse> getAllSavedPosts(String rootUserId) {
        List<SavedPost> savedPostList = savedPostRepository.findAllByUserId(Sort.by(Sort.Direction.DESC, "savedAt"), rootUserId);
        List<PostOnPersonalWallResponse> postOnPersonalWallResponses = new ArrayList<>();
        for (SavedPost savedPost : savedPostList) {
            postOnPersonalWallResponses.add(
                    PostOnPersonalWallResponse.builder()
                            .id(savedPost.getPostId())
                            .createdAt(savedPost.getSavedAt())
                            .representImage(postContentRepository.findFirstByPostId(savedPost.getPostId()).getImageId())
                            .numberOfLikes(likePostRepository.findByPostId(savedPost.getPostId()).size())
                            .numberOfComments(commentRepository.findAllByPostId(savedPost.getPostId()).size())
                            .build()
            );
        }
        return  postOnPersonalWallResponses;
    }
}
