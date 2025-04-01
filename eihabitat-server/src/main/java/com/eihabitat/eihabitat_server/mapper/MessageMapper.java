package com.eihabitat.eihabitat_server.mapper;

import com.eihabitat.eihabitat_server.dto.response.MessageResponse;
import com.eihabitat.eihabitat_server.entity.Message;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageResponse toMessageResponse(Message message);
}
