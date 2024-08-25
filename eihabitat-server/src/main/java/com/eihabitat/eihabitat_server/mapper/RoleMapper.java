package com.eihabitat.eihabitat_server.mapper;

import com.eihabitat.eihabitat_server.dto.request.RoleCreationReq;
import com.eihabitat.eihabitat_server.dto.response.RoleResponse;
import com.eihabitat.eihabitat_server.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleCreationReq role);
    RoleResponse toRoleResponse(Role role);
}
