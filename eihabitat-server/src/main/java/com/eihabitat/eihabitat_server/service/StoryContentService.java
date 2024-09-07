package com.eihabitat.eihabitat_server.service;
import com.eihabitat.eihabitat_server.dto.request.PostContentReq;
import com.eihabitat.eihabitat_server.dto.request.StoryContentReq;
import com.eihabitat.eihabitat_server.entity.Post;
import com.eihabitat.eihabitat_server.entity.PostContent;
import com.eihabitat.eihabitat_server.entity.Story;
import com.eihabitat.eihabitat_server.entity.StoryContent;
import com.eihabitat.eihabitat_server.exception.AppException;
import com.eihabitat.eihabitat_server.exception.ErrorCode;
import com.eihabitat.eihabitat_server.mapper.StoryMapper;
import com.eihabitat.eihabitat_server.repository.StoryContentRepository;
import com.eihabitat.eihabitat_server.repository.StoryRepository;
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
public class StoryContentService {
    StoryContentRepository repo;
    StoryRepository storyRepo;
    StoryMapper mapper;

    public StoryContent createStoryContent(StoryContentReq storyContentReq) {
        Story story = storyRepo.findById(storyContentReq.getStoryId()).orElseThrow(() -> new AppException(ErrorCode.STORY_NOT_FOUND));
        StoryContent storyContent = mapper.toStoryContent(storyContentReq);
        storyContent.setStoryId(story);
        return repo.save(storyContent);
    }

    public Story findStoryById(String storyId) throws Exception {
        Optional<Story> opt = storyRepo.findById(storyId);
        if(opt.isPresent()) {
            return opt.get();
        }
        throw new Exception("Story not exist with id: "+storyId);
    }
}
