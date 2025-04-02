package com.eihabitat.eihabitat_server.service;

import com.eihabitat.eihabitat_server.S3Upload.S3Service;
import com.eihabitat.eihabitat_server.dto.request.PostContentReq;
import com.eihabitat.eihabitat_server.dto.request.PostCreationReq;
import com.eihabitat.eihabitat_server.dto.request.PostUpdateReq;
import com.eihabitat.eihabitat_server.dto.response.PostContentResponse;
import com.eihabitat.eihabitat_server.dto.response.PostOnPersonalWallResponse;
import com.eihabitat.eihabitat_server.dto.response.PostResponse;
import com.eihabitat.eihabitat_server.entity.*;
import com.eihabitat.eihabitat_server.exception.AppException;
import com.eihabitat.eihabitat_server.exception.ErrorCode;
import com.eihabitat.eihabitat_server.mapper.PostMapper;
import com.eihabitat.eihabitat_server.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostService {
    UserRepository userRepo;
    PostContentRepository postContentRepo;
    CommentRepository commentRepo;
    PostRepository postRepository;
    UserLikePostRepository likePostRepo;
    UserFollowRepository userFollowRepo;
    SavedPostRepository savedPostRepo;
    StoryRepository storyRepo;
    UserSeenStoryRepository userSeenStoryRepo;
    PostMapper mapper;
    S3Service s3Service;

    public PostResponse createPost(String caption, String userId, List<MultipartFile> files) throws Exception {
        User user = userRepo.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Post post = new Post().builder()
                .caption(caption)
                .type("image")
                .createdAt(LocalDateTime.now())
                .author(user)
                .build();

        Post createdPost = postRepository.save(post);
        for(MultipartFile image : files) {
            String link = s3Service.uploadFile(image, createdPost.getId()+image.getOriginalFilename());
            postContentRepo.save(PostContent.builder()
                    .imageId(link)
                    .postId(createdPost.getId())
                    .build());
        }
        return findPostById(createdPost.getId(), user.getId());
    }

    public Post updatePost(String postId, PostUpdateReq updateRequest) {
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        mapper.updatePost(existingPost, updateRequest);
        existingPost.setCreatedAt(LocalDateTime.now());
        return postRepository.save(existingPost);
    }

    public PostResponse findPostById(String postId, String rootUserId) throws Exception {
        Post opt = postRepository.findById(postId).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        List<UserLikePost> userLikePosts = likePostRepo.findByPostId(postId);

        Set<PostContent> postContentSet = postContentRepo.findAllByPostId(opt.getId());

        User author = opt.getAuthor();

        Set<PostContentResponse> postContentResponseSet = new HashSet<>();

        for(PostContent postContent : postContentSet) {
            postContentResponseSet.add(mapper.toPostContentResponse(postContent));
        }
        PostResponse postResponse = mapper.toPostResponse(opt);
        postResponse.setAuthorId(author.getId());
        postResponse.setLikeByUser(likePostRepo.existsByUserIdAndPostId(rootUserId, postId));
        postResponse.setSavedByUser(savedPostRepo.existsByPostIdAndUserId(postId, rootUserId));

        List<Story> activeStories = storyRepo.findAllByAuthorIdAndExpiresAtAfter(Sort.by(Sort.Direction.ASC, "expiresAt"), author.getId(), LocalDateTime.now());
        if (activeStories.isEmpty()) {
            postResponse.setStory(false);
            postResponse.setNewStory(false);
        } else {
            postResponse.setStory(true);
            Story story = activeStories.getLast();
            postResponse.setNewStory(!userSeenStoryRepo.existsByUserIdAndStoryId(rootUserId, story.getId()));
        }

        if (userLikePosts.isEmpty()) {
            postResponse.setLatestUserLike(null);
            postResponse.setLatestUserLikeAvatar(null);
        } else {
            User latestUserLike = userRepo.findById(userLikePosts.getLast().getUserId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            postResponse.setLatestUserLike(latestUserLike.getProfileName());
            postResponse.setLatestUserLikeAvatar(latestUserLike.getProfileAvatar());
        }
        postResponse.setNumberOfComments(commentRepo.findAllByPostId(postId).size());
        postResponse.setPostContentSet(postContentResponseSet);
        postResponse.setAuthorProfileName(author.getProfileName());
        postResponse.setAuthorProfileAvatar(author.getProfileAvatar());
        postResponse.setAuthorUrl(author.getUserUrl());
        postResponse.setNumberOfLikes(userLikePosts.size());
        return postResponse;
    }

    public Set<PostResponse> findAllPostByUserId(String userId) throws Exception {
        Set<Post> listPost = postRepository.findAllByAuthorId(userId);
        Set<PostResponse> listPostResponse = new HashSet<>();
        for (Post post : listPost) {
            listPostResponse.add(findPostById(post.getId(), userId));
        }
        return listPostResponse;
    }

    public Set<PostOnPersonalWallResponse> findAllPostByUserProfileName(String userProfileName) throws Exception {
        Set<Post> listPost = postRepository.findAllByAuthorProfileName(Sort.by(Sort.Direction.DESC, "createdAt"), userProfileName);
        Set<PostOnPersonalWallResponse> listPostResponse = new HashSet<>();

        for (Post post : listPost) {
            List<Comment> comments = commentRepo.findAllByPostIdAndReplyTo(Sort.by(Sort.Direction.DESC, "creationDate"), post.getId(), null).stream().toList();
            new PostOnPersonalWallResponse();
            listPostResponse.add(
                    PostOnPersonalWallResponse.builder()
                            .id(post.getId())
                            .createdAt(post.getCreatedAt())
                            .representImage(postContentRepo.findFirstByPostId(post.getId()).getImageId())
                            .numberOfLikes(likePostRepo.findByPostId(post.getId()).size())
                            .numberOfComments(comments.size())
                            .build()
            );
        }

        return listPostResponse;
    }

    public String deletePost(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        postRepository.delete(post);
        return "Deleted post successfully";
    }

//    public List<PostResponse> findAllNewsFeedPosts(String rootUserId) throws Exception {
//        List<UserFollow> followeds = userFollowRepo.findByFollowerId(rootUserId);
//        Set<String> followedIds = new HashSet<>();
//        for (UserFollow userFollow : followeds) {
//            followedIds.add(userFollow.getFollowedId());
//        }
//        followedIds.add(rootUserId);
//        List<Post> listPost = postRepository.findAllByAuthorIdIn(Sort.by(Sort.Direction.DESC, "createdAt"), followedIds);
//        List<PostResponse> listPostResponse = new ArrayList<>();
//        for (Post post : listPost) {
//            PostResponse postResponse = findPostById(post.getId(), rootUserId);
//            listPostResponse.add(postResponse);
//        }
//        listPostResponse.sort((post1, post2) -> post2.getCreatedAt().compareTo(post1.getCreatedAt()));
//
//        return listPostResponse;
//    }

    public Page<PostResponse> findAllNewsFeedPosts(int page, int size, String rootUserId) throws Exception {
        List<UserFollow> followeds = userFollowRepo.findByFollowerId(rootUserId);
        Set<String> followedIds = followeds.stream()
                .map(UserFollow::getFollowedId)
                .collect(Collectors.toSet());
        followedIds.add(rootUserId);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> posts = postRepository.findAllByAuthorIdIn(pageable, followedIds);
        List<PostResponse> content = posts.getContent().stream()
                .map(post -> {
                    try {
                        return findPostById(post.getId(), rootUserId);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        return new PageImpl<>(content, pageable, posts.getTotalElements());
    }

}
