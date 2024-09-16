package com.eihabitat.eihabitat_server.mapper;

import com.eihabitat.eihabitat_server.dto.request.CommentCreationReq;
import com.eihabitat.eihabitat_server.dto.request.CommentUpdateReq;
import com.eihabitat.eihabitat_server.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "postId", ignore = true)
    Comment toComment(CommentCreationReq request);

    @Mapping(target = "postId", ignore = true)
    void updateComment(@MappingTarget Comment comment, CommentUpdateReq request);
}
