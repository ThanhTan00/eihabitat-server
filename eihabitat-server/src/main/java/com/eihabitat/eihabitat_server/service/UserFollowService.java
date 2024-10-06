package com.eihabitat.eihabitat_server.service;

import com.eihabitat.eihabitat_server.dto.request.UserFollowReq;
import com.eihabitat.eihabitat_server.dto.response.UserFollowResponse;
import com.eihabitat.eihabitat_server.dto.response.UserResponse;
import com.eihabitat.eihabitat_server.entity.User;
import com.eihabitat.eihabitat_server.entity.UserFollow;
import com.eihabitat.eihabitat_server.mapper.UserFollowMapper;
import com.eihabitat.eihabitat_server.repository.UserFollowRepository;
import com.eihabitat.eihabitat_server.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    public UserFollowResponse followUser(UserFollowReq requestDto) {
        if (requestDto.getFollowerId().equals(requestDto.getFollowedId())) {
            throw new RuntimeException("Users cannot follow themselves");
        }

        UserFollow userFollow = new UserFollow();
        userFollow.setFollowerId(requestDto.getFollowerId());
        userFollow.setFollowedId(requestDto.getFollowedId());
        userFollow.setFollowedAt(new Date());

        userFollow = userFollowRepository.save(userFollow);

        return userFollowMapper.userFollowToResponseDto(userFollow);
    }

    public UserFollowResponse unfollowUser(UserFollowReq requestDto) {
        return userFollowRepository.deleteByFollowerIdAndFollowedId(requestDto.getFollowerId(), requestDto.getFollowedId());
    }

    public List<UserResponse> getFollowers(String profileName) {
        User user = userRepository.findByProfileName(profileName)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<UserFollow> follows = userFollowRepository.findByFollowedId(user.getId());
        return follows.stream()
                .map(follow -> userRepository.findById(follow.getFollowerId()).orElse(null))
                .filter(Objects::nonNull)
                .map(userFollowMapper::userToResponseDto)
                .collect(Collectors.toList());
    }

    public List<UserResponse> getFollowing(String profileName) {
        User user = userRepository.findByProfileName(profileName)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<UserFollow> follows = userFollowRepository.findByFollowerId(user.getId());
        return follows.stream()
                .map(follow -> userRepository.findById(follow.getFollowedId()).orElse(null))
                .filter(Objects::nonNull)
                .map(userFollowMapper::userToResponseDto)
                .collect(Collectors.toList());
    }
}
