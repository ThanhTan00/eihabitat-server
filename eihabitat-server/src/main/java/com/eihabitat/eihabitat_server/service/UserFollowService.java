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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserFollowService {
    private final UserRepository userRepository;
    private final UserFollowRepository userFollowRepository;
    private final UserFollowMapper userFollowMapper;

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

    public List<UserFollowerResponse> getFollowers(String profileName) {
        User user = userRepository.findByProfileName(profileName)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<UserFollow> follows = userFollowRepository.findByFollowedId(user.getId());
        List<UserFollowerResponse> userFollowerResponses =new ArrayList<>();
        for (UserFollow userFollow : follows) {
            User follower = userRepository.findById(userFollow.getFollowerId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            userFollowerResponses.add(userFollowMapper.toUserFollowerResponse(follower));
        }
        return userFollowerResponses;
    }

    public List<UserFollowerResponse> getFollowing(String profileName) {
        User user = userRepository.findByProfileName(profileName)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<UserFollow> follows = userFollowRepository.findByFollowerId(user.getId());
        List<UserFollowerResponse> userFollowingResponses =new ArrayList<>();
        for (UserFollow userFollow : follows) {
            User follower = userRepository.findById(userFollow.getFollowedId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            userFollowingResponses.add(userFollowMapper.toUserFollowerResponse(follower));
        }
        return userFollowingResponses;
    }
}
