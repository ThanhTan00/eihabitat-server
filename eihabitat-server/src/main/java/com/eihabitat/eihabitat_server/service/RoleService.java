package com.eihabitat.eihabitat_server.service;

import com.eihabitat.eihabitat_server.dto.request.RoleCreationReq;
import com.eihabitat.eihabitat_server.dto.response.RoleResponse;
import com.eihabitat.eihabitat_server.entity.Role;
import com.eihabitat.eihabitat_server.mapper.RoleMapper;
import com.eihabitat.eihabitat_server.repository.PermissionRepository;
import com.eihabitat.eihabitat_server.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public RoleResponse createRole (RoleCreationReq request) {
        Role role = roleMapper.toRole(request);

        var permission = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permission));

        roleRepository.save(role);
        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    public void deleteRole (String roleName) {
        roleRepository.deleteById(roleName);
    }
}
