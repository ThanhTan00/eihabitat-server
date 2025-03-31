package com.eihabitat.eihabitat_server.service;

import com.eihabitat.eihabitat_server.dto.request.UserFollowReq;
import com.eihabitat.eihabitat_server.dto.response.SuggestFollowResponse;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserFollowService {
    private final UserRepository userRepository;
    private final UserFollowRepository userFollowRepository;

    NotificationService notificationService;

    private final UserFollowMapper userFollowMapper;

    public String followUser(UserFollowReq requestDto) {
        if (requestDto.getFollowerId().equals(requestDto.getFollowedId())) {
            throw new RuntimeException("Users cannot follow themselves");
        }
        User follower = userRepository.findById(requestDto.getFollowerId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        User following = userRepository.findById(requestDto.getFollowedId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        UserFollow userFollow = userFollowMapper.toUserFollow(requestDto);
        userFollow.setFollowedAt(new Date());

        userFollow = userFollowRepository.save(userFollow);

        notificationService.sendFollowNotification(follower, following);

        return "Follow user successfully followed by " + userFollow.getFollowedId();
    }

    public String unfollowUser(UserFollowReq requestDto) {
        UserFollow userFollow = userFollowRepository.findByFollowerIdAndFollowedId(requestDto.getFollowerId(), requestDto.getFollowedId()).orElseThrow(()-> new AppException(ErrorCode.FOLLOW_RELATIONSHIP_NOT_FOUND)) ;
        userFollowRepository.delete(userFollow);
        return "User: " + requestDto.getFollowedId() +"successfully unfollowed by " + userFollow.getFollowerId();
    }

    public int getNumberOfFollowers(String userId) {
        List<UserFollow> follows = userFollowRepository.findByFollowedId(userId);
        return follows.size();
    }

    public int getNumberOfFollowing(String userId) {
        List<UserFollow> follows = userFollowRepository.findByFollowerId(userId);
        return follows.size();
    }

    public boolean getIsFollowing(String followerId, String userId) {
        return userFollowRepository.existsByFollowerIdAndFollowedId(followerId, userId);
    }

    public List<UserFollowerResponse> getFollowers(String profileName, String rootUserId) {
        User user = userRepository.findByProfileName(profileName)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        List<UserFollow> follows = userFollowRepository.findByFollowedId(user.getId());
        List<UserFollowerResponse> userFollowerResponses =new ArrayList<>();
        for (UserFollow userFollow : follows) {
            User follower = userRepository.findById(userFollow.getFollowerId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            UserFollowerResponse u = userFollowMapper.toUserFollowerResponse(follower);
            u.setFollowedByMe(getIsFollowing(rootUserId, u.getId()));
            u.setFollowMe(getIsFollowing(u.getId(),rootUserId));
            u.setUserUrl(follower.getUserUrl());
            userFollowerResponses.add(u);
        }
        return userFollowerResponses;
    }

    public List<UserFollowerResponse> getFollowing(String profileName, String rootUserId) {
        User user = userRepository.findByProfileName(profileName)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        List<UserFollow> follows = userFollowRepository.findByFollowerId(user.getId());
        List<UserFollowerResponse> userFollowingResponses =new ArrayList<>();
        for (UserFollow userFollow : follows) {
            User follower = userRepository.findById(userFollow.getFollowedId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            UserFollowerResponse u = userFollowMapper.toUserFollowerResponse(follower);
            u.setFollowedByMe(getIsFollowing(rootUserId, u.getId()));
            u.setFollowMe(getIsFollowing(u.getId(),rootUserId));
            u.setUserUrl(follower.getUserUrl());
            userFollowingResponses.add(u);
        }
        return userFollowingResponses;
    }

    public List<UserFollow> getMyFollowing(String userId) {
        return userFollowRepository.findByFollowerId(userId);
    }

    public List<UserFollow> getMyFollowers(String userId) {
        return userFollowRepository.findByFollowedId(userId);
    }

    public List<SuggestFollowResponse> suggestByUserId(String userId) {
        List<UserFollow> follows = getMyFollowing(userId);
        List<String> followingIds = follows.stream()
                .map(UserFollow::getFollowedId)
                .toList();

        List<UserFollow> folowingsOfFollowing = userFollowRepository.findAllByFollowerIdIn(followingIds);
        List<String> suggestedUserIds = new ArrayList<String>();
        List<SuggestFollowResponse> result = new ArrayList<>();

        for (UserFollow secondHopFollow : folowingsOfFollowing) {
            String suggestedUserId = secondHopFollow.getFollowedId();

            // Skip if the suggested user is already followed by A, is A themselves, or is B
            if (followingIds.contains(suggestedUserId) || suggestedUserId.equals(userId) || suggestedUserIds.contains(suggestedUserId)) {
                continue;
            }

            if (userFollowRepository.existsByFollowerIdAndFollowedId(suggestedUserId, userId)) {
                continue;
            }

            SuggestFollowResponse suggestFollowUser = userFollowMapper.toSuggestFollowResponse(userRepository.findById(suggestedUserId)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));

            User relativeUser = userRepository.findById(secondHopFollow.getFollowerId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            suggestFollowUser.setFollowedBy(relativeUser.getProfileName());
            suggestedUserIds.add(suggestedUserId);
            result.add(suggestFollowUser);

            if (result.size()==5) {
                break;
            }
        }
        return result;
    }

    public List<SuggestFollowResponse> suggestByFollowedMe(String userId) {
        List<UserFollow> follows = getMyFollowers(userId);
        List<String> followerIds = follows.stream()
                .map(UserFollow::getFollowerId)
                .toList();
        List<SuggestFollowResponse> result = new ArrayList<>();

        for (String followerId : followerIds) {
           if (userFollowRepository.existsByFollowerIdAndFollowedId(userId, followerId)){
               continue;
           }
            SuggestFollowResponse suggestFollowUser = userFollowMapper.toSuggestFollowResponse(userRepository.findById(followerId)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
            result.add(suggestFollowUser);
            if (result.size()==5) {
                break;
            }
        }
        return result;
    }
}
