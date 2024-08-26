package com.eihabitat.eihabitat_server.service;

import com.eihabitat.eihabitat_server.dto.request.StoryCreationReq;
import com.eihabitat.eihabitat_server.entity.Story;
import com.eihabitat.eihabitat_server.mapper.StoryMapper;
import com.eihabitat.eihabitat_server.repository.StoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class StoryService {
    StoryRepository repo;
    StoryMapper mapper;
    UserService userService;

    public Story createStory(StoryCreationReq storyRequest) {
        Story story = mapper.toStory(storyRequest);
        story.setTimestamp(LocalDateTime.now());
        return repo.save(story);
    }

    public Story getStoryById(String id) throws Exception {
        return repo.findById(id)
                .orElseThrow(() -> new Exception("Story not found with id: " + id));
    }

    public List<Story> getAllStories() {
        return repo.findAll();
    }

    public List<Story> getStoriesByAuthor(String author) {
        return repo.findStoryByUserId(author);
    }


    public void deleteStory(String id) throws Exception {
        Story story = getStoryById(id);
        repo.delete(story);
    }

}
