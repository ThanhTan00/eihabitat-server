package com.eihabitat.eihabitat_server.service;

import com.eihabitat.eihabitat_server.dto.request.PostContentReq;
import com.eihabitat.eihabitat_server.dto.request.PostCreationReq;
import com.eihabitat.eihabitat_server.dto.request.PostUpdateReq;
import com.eihabitat.eihabitat_server.dto.response.*;
import com.eihabitat.eihabitat_server.entity.*;
import com.eihabitat.eihabitat_server.exception.AppException;
import com.eihabitat.eihabitat_server.exception.ErrorCode;
import com.eihabitat.eihabitat_server.mapper.CommentMapper;
import com.eihabitat.eihabitat_server.mapper.PostMapper;
import com.eihabitat.eihabitat_server.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostService {
    PostRepository repo;
    PostMapper mapper;
    CommentMapper commentMapper;
    UserRepository userRepo;
    PostContentRepository postContentRepo;
    CommentRepository commentRepo;
    PostRepository postRepository;
    UserLikePostRepository userLikePostRepository;
    UserLikePostService userLikeService;
    UserService userService;

    public PostResponse createPost(PostCreationReq postRequest) throws Exception {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Post post = mapper.toPost(postRequest);

        post.setAuthor(user);
        post.setCreatedAt(LocalDateTime.now());

        Post createdPost = repo.save(post);
        for (PostContentReq p : postRequest.getPostContentReq()) {
            postContentRepo.save(PostContent.builder()
                    .imageId(p.getImageId())
                    .postId(createdPost.getId())
                    .build());
        }
        return findPostById(createdPost.getId());
    }

    public Post updatePost(String postId, PostUpdateReq updateRequest) {
        Post existingPost = repo.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Check if the user has permission to update the post
//        if (!existingPost.getAuthor().getId().equals(updateRequest.getAuthorId())) {
//            throw new AppException(ErrorCode.UNAUTHORIZED_UPDATE);
//        }

        // Update the post fields
        mapper.updatePost(existingPost, updateRequest);
        existingPost.setCreatedAt(LocalDateTime.now());
        return repo.save(existingPost);
    }

    public PostResponse findPostById(String postId) throws Exception {
        Post opt = repo.findById(postId).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        Set<PostContent> postContentSet = postContentRepo.findAllByPostId(opt.getId());

        User author = opt.getAuthor();

        Set<PostContentResponse> postContentResponseSet = new HashSet<>();

        for (PostContent postContent : postContentSet) {
            postContentResponseSet.add(mapper.toPostContentResponse(postContent));
        }
        PostResponse postResponse = mapper.toPostResponse(opt);

        Set<Comment> comments = commentRepo.findAllByPostId(postId);
        Set<CommentResponse> commentResponseSet = new HashSet<>();
        for (Comment comment : comments) {
            commentResponseSet.add(commentMapper.toCommentResponse(comment));
        }

        postResponse.setPostContentSet(postContentResponseSet);
        postResponse.setAuthorProfileName(author.getProfileName());
        postResponse.setAuthorProfileAvatar(author.getProfileAvatar());
        postResponse.setCommentSet(commentResponseSet);

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

    public String deletePost(String postId) {
        Post post = repo.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        // Optionally, you might want to check if the current user has permission to delete the post
        // if (!post.getAuthor().getId().equals(currentUserId)) {
        //     throw new AppException(ErrorCode.UNAUTHORIZED_DELETE);
        // }
        repo.delete(post);
        return "Deleted post successfully";
    }

    public PostResponse getPostWithLikes(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        PostResponse postResponse = mapper.toPostResponse(post);

        // Add likes information
        List<UserLikePostResponse> likes = userLikeService.getLikesForPost(postId);
        long likeCount = likes.size();

        // Create a set of user IDs who liked the post
        Set<String> likedUserIds = likes.stream()
                .map(UserLikePostResponse::getUserId)
                .collect(Collectors.toSet());

        // Add like information to PostResponse
        postResponse.setLikedUserIds(likedUserIds);
        postResponse.setLikeCount(likeCount);

        return postResponse;
    }

}
