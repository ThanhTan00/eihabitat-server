package com.eihabitat.eihabitat_server.service;

import com.eihabitat.eihabitat_server.dto.request.StoryCreationReq;
import com.eihabitat.eihabitat_server.dto.response.StoryResponse;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final StoryRepository storyRepository;

    public String createStory(StoryCreationReq storyRequest) {
        Story story = mapper.toStory(storyRequest);
        story.setAuthorId(storyRequest.getAuthorId());
        story.setCreatedAt(LocalDateTime.now());
        story.setExpiresAt(LocalDateTime.now().plusHours(24));
        story.setImageUrl(storyRequest.getImageUrl());
        storyRepository.save(story);
        return "Story created successfully";
    }

    public List<StoryResponse> getActiveStories(String authorId) {
        User user = userRepo.findById(authorId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        LocalDateTime now = LocalDateTime.now();
        List<Story> activeStories = storyRepository.findAllByAuthorIdAndExpiresAtAfter(authorId, now);
        log.info("Active stories: {}", activeStories);
        List<StoryResponse> responses = new ArrayList<>();
        for (Story story : activeStories) {
            StoryResponse storyResponse = mapper.toStoryResponse(story);
            storyResponse.setAuthorAvatar(user.getProfileAvatar());
            storyResponse.setAuthorProfileName(user.getProfileName());
            responses.add(storyResponse);
        }
        return responses;
    }

    @Scheduled(fixedRate = 3600000) // Run every hour
    public void deleteExpiredStories() {
        LocalDateTime now = LocalDateTime.now();
        List<Story> expiredStories = storyRepository.findByExpiresAtBefore(now);
        storyRepository.deleteAll(expiredStories);
        log.info("Deleted {} expired stories", expiredStories.size());
    }

    public Story findStoryById(String storyId) throws Exception {
        Optional<Story> opt = repo.findById(storyId);
        if(opt.isPresent()) {
            return opt.get();
        }
        throw new Exception("Story not exist with id: "+ storyId);
    }


    public String deleteStory(String id) throws Exception {
        Story story = storyRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.STORY_NOT_FOUND));
        storyRepository.delete(story);
        return "Deleted post successfully";
    }

}
