package com.eihabitat.eihabitat_server.service;

import com.eihabitat.eihabitat_server.dto.request.PostContentReq;
import com.eihabitat.eihabitat_server.dto.request.PostCreationReq;
import com.eihabitat.eihabitat_server.dto.request.PostUpdateReq;
import com.eihabitat.eihabitat_server.dto.response.AllPostResponse;
import com.eihabitat.eihabitat_server.dto.response.PostContentResponse;
import com.eihabitat.eihabitat_server.dto.response.PostResponse;
import com.eihabitat.eihabitat_server.entity.Post;
import com.eihabitat.eihabitat_server.entity.PostContent;
import com.eihabitat.eihabitat_server.entity.User;
import com.eihabitat.eihabitat_server.exception.AppException;
import com.eihabitat.eihabitat_server.exception.ErrorCode;
import com.eihabitat.eihabitat_server.mapper.PostMapper;
import com.eihabitat.eihabitat_server.repository.PostContentRepository;
import com.eihabitat.eihabitat_server.repository.PostRepository;
import com.eihabitat.eihabitat_server.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostService {
    PostRepository repo;

    PostMapper mapper;
    UserRepository userRepo;
    PostContentRepository postContentRepo;

    PostRepository postRepository;

    public PostResponse createPost(PostCreationReq postRequest) throws Exception {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Post post = mapper.toPost(postRequest);

        post.setAuthor(user);
        post.setCreatedAt(LocalDateTime.now());

        Post createdPost = repo.save(post);
        for(PostContentReq p : postRequest.getPostContentReq()) {
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

//    public List<Post> findPostByUserId(String userId) {
//        List<Post> posts = repo.findByUserId(userId);
//        return posts;
//    }

    public PostResponse findPostById(String postId) throws Exception {
        Post opt = repo.findById(postId).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        Set<PostContent> postContentSet = postContentRepo.findAllByPostId(opt.getId());

        User author = opt.getAuthor();

        Set<PostContentResponse> postContentResponseSet = new HashSet<>();

        for(PostContent postContent : postContentSet) {
            postContentResponseSet.add(mapper.toPostContentResponse(postContent));
        }
        PostResponse postResponse = mapper.toPostResponse(opt);

        postResponse.setPostContentSet(postContentResponseSet);
        postResponse.setAuthorProfileName(author.getProfileName());
        postResponse.setAuthorProfileAvatar(author.getProfileAvatar());

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
//    public Post likePost(String postId, String userId) throws Exception  {
//        UserResponse user= userService.getUser(userId);
//        Post post=findPostById(postId);
//        post.getLikedByUsers().add(user.getId());
//
//        return repo.save(post);
//    }

//    public Post editPost(Post post) throws Exception {
//        Post isPost=findPostById(post.getId());
//
//        if(post.getCaption()!=null) {
//            isPost.setCaption(post.getCaption());
//        }
//        if(post.getLocation()!=null) {
//            isPost.setLocation(post.getLocation());
//        }
//        return repo.save(isPost);
//    }
}
