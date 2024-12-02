package com.eihabitat.eihabitat_server.mapper;

import com.eihabitat.eihabitat_server.dto.request.UserFollowReq;
import com.eihabitat.eihabitat_server.dto.response.SuggestFollowResponse;
import com.eihabitat.eihabitat_server.dto.response.UserFollowerResponse;
import com.eihabitat.eihabitat_server.entity.User;
import com.eihabitat.eihabitat_server.entity.UserFollow;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserFollowMapper {
    @Mapping(target = "id", ignore = true)
    UserFollow userToUserFollow(User follower, User followed);

    UserFollow toUserFollow(UserFollowReq userFollowReq);

    UserFollowerResponse toUserFollowerResponse(User user);

    SuggestFollowResponse toSuggestFollowResponse(User user);
}
