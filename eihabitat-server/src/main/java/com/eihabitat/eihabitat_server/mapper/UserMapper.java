package com.eihabitat.eihabitat_server.mapper;

import com.eihabitat.eihabitat_server.dto.request.UserCreationReq;
import com.eihabitat.eihabitat_server.dto.request.UserUpdateReq;
import com.eihabitat.eihabitat_server.dto.response.SearchUserResponse;
import com.eihabitat.eihabitat_server.dto.response.UserDemoResponse;
import com.eihabitat.eihabitat_server.dto.response.UserResponse;
import com.eihabitat.eihabitat_server.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser (UserCreationReq req);
    UserResponse toUserResponse (User user);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nationality", ignore = true)
    void updateUser (@MappingTarget User user, UserUpdateReq req);

    UserDemoResponse toUserDemoResponse (User user);

    SearchUserResponse toSearchUserResponse (User user);
}
