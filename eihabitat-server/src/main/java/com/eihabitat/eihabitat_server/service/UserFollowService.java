package com.eihabitat.eihabitat_server.service;

import com.eihabitat.eihabitat_server.dto.request.UserFollowReq;
import com.eihabitat.eihabitat_server.dto.response.UserFollowerResponse;
import com.eihabitat.eihabitat_server.entity.User;
import com.eihabitat.eihabitat_server.entity.UserFollow;
import com.eihabitat.eihabitat_server.exception.AppException;
import com.eihabitat.eihabitat_server.exception.ErrorCode;
import com.eihabitat.eihabitat_server.mapper.UserFollowMapper;
import com.eihabitat.eihabitat_server.repository.UserFollowRepository;
import com.eihabitat.eihabitat_server.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserFollowService {
    private final UserRepository userRepository;
    private final UserFollowRepository userFollowRepository;
    private final UserFollowMapper userFollowMapper;

    public List<UserFollowerResponse> suggestFollowers(String profileName, String rootUserId) {
        // Step 1: Find the user by profile name
        User user = userRepository.findByProfileName(profileName)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Step 2: Get the list of users the current user is following
        List<UserFollow> follows = userFollowRepository.findByFollowerId(user.getId());
        List<String> followingIds = follows.stream()
                .map(UserFollow::getFollowedId)
                .toList();

        // Step 3: Find users followed by the user's followings (two-hop connection)
        List<UserFollowerResponse> suggestedFollowers = new ArrayList<>();
        Set<String> suggestedUserIds = new HashSet<>(); // To avoid duplicates

        for (String followingId : followingIds) {
            // Find users that B (followingId) follows
            List<UserFollow> secondHopFollows = userFollowRepository.findByFollowerId(followingId);

            for (UserFollow secondHopFollow : secondHopFollows) {
                String suggestedUserId = secondHopFollow.getFollowedId();

                // Skip if the suggested user is already followed by A, is A themselves, or is B
                if (followingIds.contains(suggestedUserId) || suggestedUserId.equals(user.getId()) || suggestedUserIds.contains(suggestedUserId)) {
                    continue;
                }

                // Fetch the suggested user
                User suggestedUser = userRepository.findById(suggestedUserId)
                        .orElseThrow(() -> new RuntimeException("Suggested user not found"));

                // Map to UserFollowerResponse
                UserFollowerResponse response = userFollowMapper.toUserFollowerResponse(suggestedUser);

                // Check mutual follow status
                if (userFollowRepository.existsByFollowerIdAndFollowedId(rootUserId, suggestedUserId)) {
                    response.setFollowedByMe(true);
                }
                if (userFollowRepository.existsByFollowerIdAndFollowedId(suggestedUserId, rootUserId)) {
                    response.setFollowMe(true);
                }

                // Set additional fields
                response.setUserUrl(suggestedUser.getUserUrl());
                suggestedFollowers.add(response);

                // Track processed users
                suggestedUserIds.add(suggestedUserId);
            }
        }

        return suggestedFollowers;
    }


    public String followUser(UserFollowReq requestDto) {
        if (requestDto.getFollowerId().equals(requestDto.getFollowedId())) {
            throw new RuntimeException("Users cannot follow themselves");
        }

        UserFollow userFollow = userFollowMapper.toUserFollow(requestDto);
        userFollow.setFollowedAt(new Date());

        userFollow = userFollowRepository.save(userFollow);

        return "Follow user successfully followed by " + userFollow.getFollowedId();
    }

    public String unfollowUser(UserFollowReq requestDto) {
        UserFollow userFollow = userFollowRepository.findByFollowerIdAndFollowedId(requestDto.getFollowerId(), requestDto.getFollowedId()).orElseThrow(()-> new AppException(ErrorCode.FOLLOW_RELATIONSHIP_NOT_FOUND)) ;
        userFollowRepository.delete(userFollow);
        return "User: " + requestDto.getFollowedId() +"successfully unfollowed by " + userFollow.getFollowerId();
    }

    public List<UserFollowerResponse> getFollowers(String profileName, String rootUserId) {
        User user = userRepository.findByProfileName(profileName)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<UserFollow> follows = userFollowRepository.findByFollowedId(user.getId());
        List<UserFollowerResponse> userFollowerResponses =new ArrayList<>();
        for (UserFollow userFollow : follows) {
            User follower = userRepository.findById(userFollow.getFollowerId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            UserFollowerResponse u = userFollowMapper.toUserFollowerResponse(follower);
            if (userFollowRepository.existsByFollowerIdAndFollowedId(rootUserId, follower.getId())){
                u.setFollowedByMe(true);
            }
            if (userFollowRepository.existsByFollowerIdAndFollowedId(follower.getId(), rootUserId)) {
                u.setFollowMe(true);
            }
            u.setUserUrl(follower.getUserUrl());
            userFollowerResponses.add(u);
        }
        return userFollowerResponses;
    }

    public List<UserFollowerResponse> getFollowing(String profileName, String rootUserId) {
        User user = userRepository.findByProfileName(profileName)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<UserFollow> follows = userFollowRepository.findByFollowerId(user.getId());
        List<UserFollowerResponse> userFollowingResponses =new ArrayList<>();
        for (UserFollow userFollow : follows) {
            User follower = userRepository.findById(userFollow.getFollowedId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            UserFollowerResponse u = userFollowMapper.toUserFollowerResponse(follower);
            if (userFollowRepository.existsByFollowerIdAndFollowedId(rootUserId, follower.getId())){
                u.setFollowedByMe(true);
            }
            if (userFollowRepository.existsByFollowerIdAndFollowedId(follower.getId(), rootUserId)) {
                u.setFollowMe(true);
            }
            u.setUserUrl(follower.getUserUrl());
            userFollowingResponses.add(u);
        }
        return userFollowingResponses;
    }
}
