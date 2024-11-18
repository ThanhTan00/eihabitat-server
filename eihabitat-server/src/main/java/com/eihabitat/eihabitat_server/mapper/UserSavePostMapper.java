package com.eihabitat.eihabitat_server.mapper;

import com.eihabitat.eihabitat_server.dto.request.UserLikePostReq;
import com.eihabitat.eihabitat_server.dto.request.UserSavePostReq;
import com.eihabitat.eihabitat_server.dto.response.UserLikePostResponse;
import com.eihabitat.eihabitat_server.dto.response.UserSavePostResponse;
import com.eihabitat.eihabitat_server.entity.UserLikePost;
import com.eihabitat.eihabitat_server.entity.UserSavePost;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserSavePostMapper {
    UserSavePostMapper INSTANCE = Mappers.getMapper(UserSavePostMapper.class);

    @Mapping(target = "userId", source = "request.userId")
    @Mapping(target = "postId", source = "request.postId")
    UserSavePost toEntity(UserSavePostReq request);

    UserSavePostResponse toResponse(UserSavePost savePost);
}
