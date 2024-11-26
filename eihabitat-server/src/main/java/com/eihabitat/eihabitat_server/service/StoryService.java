package com.eihabitat.eihabitat_server.service;

import com.eihabitat.eihabitat_server.S3Upload.S3Service;
import com.eihabitat.eihabitat_server.dto.request.StoryCreationReq;
import com.eihabitat.eihabitat_server.dto.response.StoryContentResponse;
import com.eihabitat.eihabitat_server.dto.response.StoryResponse;
import com.eihabitat.eihabitat_server.entity.*;
import com.eihabitat.eihabitat_server.exception.AppException;
import com.eihabitat.eihabitat_server.exception.ErrorCode;
import com.eihabitat.eihabitat_server.mapper.StoryMapper;
import com.eihabitat.eihabitat_server.repository.StoryContentRepo;
import com.eihabitat.eihabitat_server.repository.StoryRepository;
import com.eihabitat.eihabitat_server.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class StoryService {
    StoryRepository repo;
    StoryMapper mapper;
    UserRepository userRepo;
    StoryContentRepo storyContentRepo;
    S3Service s3Service;

    private final StoryRepository storyRepository;

    public String createStory(String authorId, List<MultipartFile> files) throws IOException {
        User user = userRepo.findById(authorId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Story story = Story.builder()
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusHours(24)) // If you want stories to expire
                .authorId(user.getId())
                .build();
        Story createdStory = repo.save(story);

        for (MultipartFile image : files) {
            String link = s3Service.uploadFile(image, createdStory.getId() + image.getOriginalFilename());
            storyContentRepo.save(StoryContent.builder()
                    .imageId(link)
                    .storyId(createdStory.getId())
                    .build());
        }
        return "Story Created Successfully";
    }

    public List<StoryResponse> getActiveStories(String authorId) {
        // Fetch the user
        User user = userRepo.findById(authorId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Fetch active stories for the author
        List<Story> activeStories = storyRepository.findAllByAuthorIdAndExpiresAtAfter(authorId, LocalDateTime.now());
        log.info("Found {} active stories for authorId {}: {}", activeStories.size(), authorId, activeStories);

        // Map stories to StoryResponse
        return activeStories.stream()
                .map(story -> {
                    // Fetch associated StoryContent for each Story
                    List<StoryContent> storyContents = storyContentRepo.findByStoryId(story.getId());

                    // Map StoryContent to StoryContentResponse
                    Set<StoryContentResponse> contentResponses = storyContents.stream()
                            .map(content -> {
                                StoryContentResponse response = new StoryContentResponse();
                                response.setImageId(content.getImageId());
                                return response;
                            })
                            .collect(Collectors.toSet());

                    // Create StoryResponse and populate fields
                    StoryResponse storyResponse = mapper.toStoryResponse(story);
                    storyResponse.setAuthorAvatar(user.getProfileAvatar());
                    storyResponse.setAuthorProfileName(user.getProfileName());
                    storyResponse.setExpiredAt(storyResponse.getCreatedAt().plusHours(24));
                    storyResponse.setStoryContentSet(contentResponses);

                    return storyResponse;
                })
                .collect(Collectors.toList());
    }



    public List<StoryResponse> getActiveStories1(String authorId) {
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

    public StoryResponse findStoryById(String storyId) {
        Story story = repo.findById(storyId)
                .orElseThrow(() -> new AppException(ErrorCode.STORY_NOT_FOUND));

        List<StoryContent> storyContents = storyContentRepo.findByStoryId(story.getId());

        User author = userRepo.findById(story.getAuthorId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Map StoryContent to StoryContentResponse
        Set<StoryContentResponse> contentResponses = storyContents.stream()
                .map(content -> {
                    StoryContentResponse response = new StoryContentResponse();
                    response.setImageId(content.getImageId());
                    return response;
                })
                .collect(Collectors.toSet());

        // Map to StoryResponse
        StoryResponse response = new StoryResponse();
        response.setId(story.getId());
        response.setCreatedAt(story.getCreatedAt());
        response.setStoryContentSet(contentResponses);
        response.setAuthorAvatar(author.getProfileAvatar()); // Assuming User has an avatar URL field
        response.setAuthorProfileName(author.getProfileName());

        return response;
    }

    public String deleteStory(String id) throws Exception {
        Story story = storyRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.STORY_NOT_FOUND));
        storyRepository.delete(story);
        return "Deleted post successfully";
    }

}
