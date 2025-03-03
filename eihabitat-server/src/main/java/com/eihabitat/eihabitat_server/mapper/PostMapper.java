package com.eihabitat.eihabitat_server.mapper;

import com.eihabitat.eihabitat_server.dto.request.PostCreationReq;
import com.eihabitat.eihabitat_server.dto.request.PostUpdateReq;
import com.eihabitat.eihabitat_server.dto.request.SavePostReq;
import com.eihabitat.eihabitat_server.dto.response.PostContentResponse;
import com.eihabitat.eihabitat_server.dto.response.PostResponse;
import com.eihabitat.eihabitat_server.entity.Post;
import com.eihabitat.eihabitat_server.entity.PostContent;
import com.eihabitat.eihabitat_server.entity.SavedPost;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(target = "author", ignore = true)
    Post toPost(PostCreationReq request);

    @Mapping(target = "authorProfileName", ignore = true)
    @Mapping(target = "authorProfileAvatar", ignore = true)
    PostResponse toPostResponse(Post post);

    @Mapping(target = "author", ignore = true)
    void updatePost (@MappingTarget Post post, PostUpdateReq request);

    PostContentResponse toPostContentResponse(PostContent postContents);

    SavedPost toSavedPost (SavePostReq req);
}
