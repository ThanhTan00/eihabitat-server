package com.eihabitat.eihabitat_server.mapper;

import com.eihabitat.eihabitat_server.dto.request.StoryContentReq;
import com.eihabitat.eihabitat_server.dto.request.StoryCreationReq;
import com.eihabitat.eihabitat_server.dto.response.StoryResponse;
import com.eihabitat.eihabitat_server.entity.Story;
import com.eihabitat.eihabitat_server.entity.StoryContent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface StoryMapper {
    @Mapping(target = "author", ignore = true)
    Story toStory(StoryCreationReq request);
    @Mapping(target = "storyId", ignore = true)
    StoryContent toStoryContent(StoryContentReq request);
    StoryResponse toStoryResponse(Story story);
}
