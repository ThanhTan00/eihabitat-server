package com.eihabitat.eihabitat_server.mapper;

import com.eihabitat.eihabitat_server.dto.response.NotificationResponse;
import com.eihabitat.eihabitat_server.entity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationResponse toNotificationResponse(Notification notification);
}
