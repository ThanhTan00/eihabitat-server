package com.eihabitat.eihabitat_server.mapper;

import com.eihabitat.eihabitat_server.dto.request.PermissionCreationReq;
import com.eihabitat.eihabitat_server.dto.response.PermissionResponse;
import com.eihabitat.eihabitat_server.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionCreationReq request);
    PermissionResponse toPermissionResponse(Permission permission);
}
