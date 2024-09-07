package com.eihabitat.eihabitat_server.service;

import com.eihabitat.eihabitat_server.dto.request.PostContentReq;
import com.eihabitat.eihabitat_server.entity.Post;
import com.eihabitat.eihabitat_server.entity.PostContent;
import com.eihabitat.eihabitat_server.exception.AppException;
import com.eihabitat.eihabitat_server.exception.ErrorCode;
import com.eihabitat.eihabitat_server.mapper.PostMapper;
import com.eihabitat.eihabitat_server.repository.PostContentRepository;
import com.eihabitat.eihabitat_server.repository.PostRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostContentService {
    PostContentRepository repo;
    PostRepository postRepo;
    PostMapper mapper;

    public PostContent createPostContent(PostContentReq postContentReq) {
        Post post = postRepo.findById(postContentReq.getPostId()).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        PostContent postContent = mapper.toPostContent(postContentReq);
        postContent.setPostId(post);
        return repo.save(postContent);
    }

    public Post findPostById(String postId) throws Exception {
        Optional<Post> opt = postRepo.findById(postId);
        if(opt.isPresent()) {
            return opt.get();
        }
        throw new Exception("Post not exist with id: "+postId);
    }

}
