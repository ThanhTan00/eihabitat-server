package com.eihabitat.eihabitat_server.service;

import com.eihabitat.eihabitat_server.dto.request.PostCreationReq;
import com.eihabitat.eihabitat_server.dto.request.PostUpdateReq;
import com.eihabitat.eihabitat_server.entity.Post;
import com.eihabitat.eihabitat_server.entity.User;
import com.eihabitat.eihabitat_server.exception.AppException;
import com.eihabitat.eihabitat_server.exception.ErrorCode;
import com.eihabitat.eihabitat_server.mapper.PostMapper;
import com.eihabitat.eihabitat_server.repository.PostRepository;
import com.eihabitat.eihabitat_server.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostService {
    PostRepository repo;

    @Autowired
    PostMapper mapper;
    UserRepository userRepo;

    public Post createPost(PostCreationReq postRequest) {
        User user = userRepo.findById(postRequest.getAuthor()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Post post = mapper.toPost(postRequest);
        post.setAuthor(user);
        post.setCreatedAt(LocalDateTime.now());
        return repo.save(post);
    }

    public Post updatePost(String postId, PostUpdateReq postRequest) throws Exception {
        Post existingPost = findPostById(postId);
        mapper.updatePost(existingPost, postRequest);
        return repo.save(existingPost);
    }

//    public List<Post> findPostByUserId(String userId) {
//        List<Post> posts = repo.findByUserId(userId);
//        return posts;
//    }

    public Post findPostById(String postId) throws Exception {
        Optional<Post> opt = repo.findById(postId);
        if(opt.isPresent()) {
            return opt.get();
        }
        throw new Exception("Post not exist with id: "+postId);
    }

//    public List<Post> findAllPost() throws Exception {
//        List<Post> posts = repo.findAll();
//        if(posts.size()>0) {
//            return posts;
//        }
//        throw new Exception("Post Not Exist");
//    }

    public void deletePost(String postId) throws Exception {
        Post post = findPostById(postId);
        repo.delete(post);
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
