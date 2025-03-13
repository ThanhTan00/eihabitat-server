package com.eihabitat.eihabitat_server.service;

import com.eihabitat.eihabitat_server.S3Upload.S3Service;
import com.eihabitat.eihabitat_server.dto.request.StoryCreationReq;
import com.eihabitat.eihabitat_server.dto.response.FollowingStoryResponse;
import com.eihabitat.eihabitat_server.dto.response.StoryResponse;
import com.eihabitat.eihabitat_server.entity.Story;
import com.eihabitat.eihabitat_server.entity.User;
import com.eihabitat.eihabitat_server.entity.UserFollow;
import com.eihabitat.eihabitat_server.entity.UserSeenStory;
import com.eihabitat.eihabitat_server.exception.AppException;
import com.eihabitat.eihabitat_server.exception.ErrorCode;
import com.eihabitat.eihabitat_server.mapper.StoryMapper;
import com.eihabitat.eihabitat_server.repository.StoryRepository;
import com.eihabitat.eihabitat_server.repository.UserFollowRepository;
import com.eihabitat.eihabitat_server.repository.UserRepository;
import com.eihabitat.eihabitat_server.repository.UserSeenStoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
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
    UserFollowRepository userFollowRepo;
    StoryRepository storyRepo;
    UserSeenStoryRepository userSeenStoryRepo;
    S3Service s3Service;
    private final UserFollowRepository userFollowRepository;

    public StoryResponse createStory(StoryCreationReq storyRequest) throws IOException {
        Story story = mapper.toStory(storyRequest);;
        LocalDateTime now = LocalDateTime.now();
        story.setCreatedAt(now);
        story.setExpiresAt(now.plusHours(24));
        story.setSeenId(new ArrayList<String>());
        String link = s3Service.uploadFile(storyRequest.getImageFile(), LocalDateTime.now()+storyRequest.getImageFile().getOriginalFilename());
        story.setImageUrl(link);
        return mapper.toStoryResponse(storyRepo.save(story));
    }

    public List<StoryResponse> getActiveStories(String authorId) {
        User user = userRepo.findById(authorId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        LocalDateTime now = LocalDateTime.now();
        List<Story> activeStories = storyRepo.findAllByAuthorIdAndExpiresAtAfter(Sort.by(Sort.Direction.ASC, "expiresAt"), authorId, now);
        List<StoryResponse> responses = new ArrayList<>();
        for (Story story : activeStories) {
            StoryResponse storyResponse = mapper.toStoryResponse(story);
            storyResponse.setAuthorAvatar(user.getProfileAvatar());
            storyResponse.setAuthorName(user.getProfileName());
            responses.add(storyResponse);
        }
        return responses;
    }

//    @Scheduled(fixedRate = 3600000) // Run every hour
//    public void deleteExpiredStories() {
//        LocalDateTime now = LocalDateTime.now();
//        List<Story> expiredStories = storyRepository.findByExpiresAtBefore(now);
//        storyRepository.deleteAll(expiredStories);
//        log.info("Deleted {} expired stories", expiredStories.size());
//    }

    public Story findStoryById(String storyId) throws Exception {
        Optional<Story> opt = repo.findById(storyId);
        if(opt.isPresent()) {
            return opt.get();
        }
        throw new Exception("Story not exist with id: "+ storyId);
    }

    public String deleteStory(String id) throws Exception {
        Story story = storyRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.STORY_NOT_FOUND));
        storyRepo.delete(story);
        return "Deleted post successfully";
    }

    public List<FollowingStoryResponse> getFollowingNewStory(String userId) throws Exception {
        List<UserFollow> followeds = userFollowRepo.findByFollowerId(userId);
        LocalDateTime now = LocalDateTime.now();
        List<FollowingStoryResponse> followingStoryResponses = new ArrayList<>();

        for (UserFollow followed : followeds) {
            List<Story> activeStories = storyRepo.findAllByAuthorIdAndExpiresAtAfter(Sort.by(Sort.Direction.ASC, "expiresAt"), followed.getFollowedId(), now);
            if (activeStories.isEmpty()) {
                continue;
            }
            Story s = activeStories.getLast();
            User user = userRepo.findById(followed.getFollowedId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            FollowingStoryResponse followingStoryResponse = FollowingStoryResponse.builder()
                    .authorId(user.getId())
                    .authorName(user.getProfileName())
                    .authorAvatar(user.getProfileAvatar())
                    .build();
            followingStoryResponse.setNewStory(!userSeenStoryRepo.existsByUserIdAndStoryId(userId, s.getId()));
            followingStoryResponses.add(followingStoryResponse);
        }
        followingStoryResponses.sort(Comparator.comparingInt(p -> p.isNewStory() ? 0 : 1));
        Story myStory = storyRepo.getFirstByAuthorIdAndExpiresAtAfter(userId, now);
        if (myStory != null) {
            User me = userRepo.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            followingStoryResponses.addFirst(FollowingStoryResponse.builder().authorId(userId).authorAvatar(me.getProfileAvatar()).authorName(me.getProfileName()).isNewStory(true).build());
        }
        return followingStoryResponses;
    }

    public String seenStory(String storyId, String userId) throws Exception {
        if (!userSeenStoryRepo.existsByUserIdAndStoryId(userId, storyId)) {
            userSeenStoryRepo.save(UserSeenStory.builder().userId(userId).storyId(storyId).build());
        }
        return "Seen story successfully";
    }
}
