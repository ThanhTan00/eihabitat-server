package com.eihabitat.eihabitat_server.mapper;

import com.eihabitat.eihabitat_server.dto.response.UserFollowResponse;
import com.eihabitat.eihabitat_server.dto.response.UserResponse;
import com.eihabitat.eihabitat_server.entity.User;
import com.eihabitat.eihabitat_server.entity.UserFollow;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserFollowMapper {
    @Mapping(target = "id", ignore = true)
    UserFollow userToUserFollow(User follower, User followed);

    UserFollowResponse userFollowToResponseDto(UserFollow userFollow);

    UserResponse userToResponseDto(User user);
}
