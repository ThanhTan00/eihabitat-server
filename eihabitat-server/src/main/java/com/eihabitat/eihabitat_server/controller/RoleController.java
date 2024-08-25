package com.eihabitat.eihabitat_server.controller;

import com.eihabitat.eihabitat_server.dto.request.ApiResponse;
import com.eihabitat.eihabitat_server.dto.request.RoleCreationReq;
import com.eihabitat.eihabitat_server.dto.response.RoleResponse;
import com.eihabitat.eihabitat_server.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/roles")
public class RoleController {
    RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> createRole (@RequestBody RoleCreationReq request){
        return ApiResponse.<RoleResponse>builder()
                .data(roleService.createRole(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAllPermissions() {
        return  ApiResponse.<List<RoleResponse>>builder()
                .data(roleService.getAllRoles())
                .build();
    }

    @DeleteMapping("/{role}")
    ApiResponse<Void> deletePermission(@PathVariable String role){
        roleService.deleteRole(role);
        return ApiResponse.<Void>builder().build();
    }

}
