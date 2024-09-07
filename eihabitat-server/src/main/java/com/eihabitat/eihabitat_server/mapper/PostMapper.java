package com.eihabitat.eihabitat_server.mapper;

import com.eihabitat.eihabitat_server.dto.request.PostContentReq;
import com.eihabitat.eihabitat_server.dto.request.PostCreationReq;
import com.eihabitat.eihabitat_server.dto.request.PostUpdateReq;
import com.eihabitat.eihabitat_server.dto.response.PostResponse;
import com.eihabitat.eihabitat_server.entity.Post;
import com.eihabitat.eihabitat_server.entity.PostContent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(target = "author", ignore = true)
    Post toPost(PostCreationReq request);
    @Mapping(target = "postId", ignore = true)
    PostContent toPostContent(PostContentReq request);
    PostResponse toPostResponse(Post post);

    @Mapping(target = "author", ignore = true)
    void updatePost (@MappingTarget Post post, PostUpdateReq request);
}
