package com.eihabitat.eihabitat_server.mapper;

import com.eihabitat.eihabitat_server.dto.request.StoryCreationReq;
import com.eihabitat.eihabitat_server.dto.response.StoryResponse;
import com.eihabitat.eihabitat_server.entity.Story;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface StoryMapper {
    @Mapping(target = "author", ignore = true)
    Story toStory(StoryCreationReq request);

    StoryResponse toStoryResponse(Story story);
}
