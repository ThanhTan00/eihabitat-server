package com.eihabitat.eihabitat_server.service;

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
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    PostMapper mapper;

    public PostResponse createPost(PostCreationReq postRequest) throws Exception {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepo.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Post post = mapper.toPost(postRequest);

        post.setAuthor(user);
        post.setCreatedAt(LocalDateTime.now());

        Post createdPost = postRepository.save(post);
        for(PostContentReq p : postRequest.getPostContentReq()) {
            postContentRepo.save(PostContent.builder()
                    .imageId(p.getImageId())
                    .postId(createdPost.getId())
                    .build());
        }
        return findPostById(createdPost.getId());
    }

    public Post updatePost(String postId, PostUpdateReq updateRequest) {
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        mapper.updatePost(existingPost, updateRequest);
        existingPost.setCreatedAt(LocalDateTime.now());
        return postRepository.save(existingPost);
    }

    public PostResponse findPostById(String postId) throws Exception {
        Post opt = postRepository.findById(postId).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        List<UserLikePost> userLikePosts = likePostRepo.findByPostId(postId);

        Set<PostContent> postContentSet = postContentRepo.findAllByPostId(opt.getId());

        User author = opt.getAuthor();

        Set<PostContentResponse> postContentResponseSet = new HashSet<>();

        for(PostContent postContent : postContentSet) {
            postContentResponseSet.add(mapper.toPostContentResponse(postContent));
        }
        PostResponse postResponse = mapper.toPostResponse(opt);

        Set<Comment> comments = commentRepo.findAllByPostId(Sort.by(Sort.Direction.DESC, "creationDate"), postId);


//        Set<CommentResponse> commentResponseSet = new HashSet<>();
//        for (Comment comment : comments) {
//            User u = userRepo.findById(comment.getOwnerId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
//            CommentResponse commentResponse = commentMapper.toCommentResponse(comment);
//            commentResponse.setOwnerProfileName(u.getProfileName());
//            commentResponse.setOwnerAvatar(u.getProfileAvatar());
//            commentResponseSet.add(commentResponse);
//        }
        if (userLikePosts.isEmpty()) {
            postResponse.setLatestUserLike(null);
            postResponse.setLatestUserLikeAvatar(null);
        } else {
            User latestUserLike = userRepo.findById(userLikePosts.getLast().getUserId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            log.info("latest liked by: " + latestUserLike.toString());
            postResponse.setLatestUserLike(latestUserLike.getProfileName());
            postResponse.setLatestUserLikeAvatar(latestUserLike.getProfileAvatar());
        }
        postResponse.setNumberOfComments(comments.size());
        postResponse.setPostContentSet(postContentResponseSet);
        postResponse.setAuthorProfileName(author.getProfileName());
        postResponse.setAuthorProfileAvatar(author.getProfileAvatar());
//        postResponse.setCommentSet(commentResponseSet);
        postResponse.setNumberOfLikes(userLikePosts.size());
        return postResponse;
    }

    public Set<PostResponse> findAllPostByUserId(String userId) throws Exception {
        Set<Post> listPost = postRepository.findAllByAuthorId(userId);
        Set<PostResponse> listPostResponse = new HashSet<>();
        for (Post post : listPost) {
            listPostResponse.add(findPostById(post.getId()));
        }
        return listPostResponse;
    }

    public Set<PostOnPersonalWallResponse> findAllPostByUserProfileName(String userProfileName) throws Exception {
        Set<Post> listPost = postRepository.findAllByAuthorProfileName(Sort.by(Sort.Direction.DESC, "createdAt"), userProfileName);
        Set<PostOnPersonalWallResponse> listPostResponse = new HashSet<>();

        for (Post post : listPost) {
            List<PostContent> postContents = postContentRepo.findAllByPostId(post.getId()).stream().toList();
            List<UserLikePost> userLikePosts = likePostRepo.findByPostId(post.getId());
            List<Comment> comments = commentRepo.findAllByPostId(Sort.by(Sort.Direction.DESC, "creationDate"), post.getId()).stream().toList();
            new PostOnPersonalWallResponse();
            listPostResponse.add(
                    PostOnPersonalWallResponse.builder()
                            .id(post.getId())
                            .representImage(postContents.getFirst().getImageId())
                            .numberOfLikes(userLikePosts.size())
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

    public Set<PostResponse> findAllNewsFeedPosts(String rootUserId) throws Exception {
        List<UserFollow> followeds = userFollowRepo.findByFollowerId(rootUserId);
        Set<String> followedIds = new HashSet<>();
        for (UserFollow userFollow : followeds) {
            followedIds.add(userFollow.getFollowedId());
        }
        log.info("followed ids: " + followedIds.toString());
        Set<Post> listPost = postRepository.findAllByAuthorIdIn(followedIds);

        log.info("list posts: " + listPost.toString());
        Set<PostResponse> listPostResponse = new HashSet<>();
        for (Post post : listPost) {
            listPostResponse.add(findPostById(post.getId()));
        }
        return listPostResponse;
    }

}
