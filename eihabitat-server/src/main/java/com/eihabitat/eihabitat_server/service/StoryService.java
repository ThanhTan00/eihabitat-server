package com.eihabitat.eihabitat_server.service;

import com.eihabitat.eihabitat_server.dto.request.PostCreationReq;
import com.eihabitat.eihabitat_server.dto.request.StoryCreationReq;
import com.eihabitat.eihabitat_server.entity.Post;
import com.eihabitat.eihabitat_server.entity.Story;
import com.eihabitat.eihabitat_server.entity.User;
import com.eihabitat.eihabitat_server.exception.AppException;
import com.eihabitat.eihabitat_server.exception.ErrorCode;
import com.eihabitat.eihabitat_server.mapper.StoryMapper;
import com.eihabitat.eihabitat_server.repository.StoryRepository;
import com.eihabitat.eihabitat_server.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class StoryService {
    StoryRepository repo;
    StoryMapper mapper;
    UserRepository userRepo;

    public Story createStory(StoryCreationReq storyRequest) {
        User user = userRepo.findById(storyRequest.getAuthor()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Story story = mapper.toStory(storyRequest);
        story.setAuthor(user);
        story.setCreatedAt(LocalDateTime.now());
        return repo.save(story);
    }

    public Story findStoryById(String storyId) throws Exception {
        Optional<Story> opt = repo.findById(storyId);
        if(opt.isPresent()) {
            return opt.get();
        }
        throw new Exception("Story not exist with id: "+ storyId);
    }

//    public List<Story> getAllStories() {
//        return repo.findAll();
//    }
//
//    public List<Story> getStoriesByAuthor(String author) {
//        return repo.findStoryByUserId(author);
//    }


    public void deleteStory(String id) throws Exception {
        Story story = findStoryById(id);
        repo.delete(story);
    }

}
