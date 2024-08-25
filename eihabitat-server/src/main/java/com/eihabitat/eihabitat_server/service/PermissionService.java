package com.eihabitat.eihabitat_server.service;

import com.eihabitat.eihabitat_server.dto.request.PermissionCreationReq;
import com.eihabitat.eihabitat_server.dto.response.PermissionResponse;
import com.eihabitat.eihabitat_server.entity.Permission;
import com.eihabitat.eihabitat_server.mapper.PermissionMapper;
import com.eihabitat.eihabitat_server.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class PermissionService {

    PermissionRepository permissionRepository;

    PermissionMapper permissionMapper;

    public PermissionResponse createPermission(PermissionCreationReq request) {
        Permission permission = permissionMapper.toPermission(request);
        permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAllPermissions() {
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public void deletePermission(String permissionName) {
        permissionRepository.deleteById(permissionName);
    }
}
