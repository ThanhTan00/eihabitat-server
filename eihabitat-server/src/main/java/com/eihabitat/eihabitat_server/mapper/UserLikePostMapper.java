package com.eihabitat.eihabitat_server.mapper;

import com.eihabitat.eihabitat_server.dto.request.UserLikePostReq;
import com.eihabitat.eihabitat_server.dto.response.UserLikePostResponse;
import com.eihabitat.eihabitat_server.entity.UserLikePost;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserLikePostMapper {
    @Mapping(target = "id", ignore = true)
    UserLikePost toUserLikePost(UserLikePostReq request);

    UserLikePostResponse toUserLikePostResponse(UserLikePost userLikePost);


    // ignore null value in the DTO
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserLikePostFromDto(UserLikePostReq dto, @MappingTarget UserLikePost entity);
}
