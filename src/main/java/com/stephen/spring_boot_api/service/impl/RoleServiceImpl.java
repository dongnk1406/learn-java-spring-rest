package com.stephen.spring_boot_api.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.stephen.spring_boot_api.dto.request.RoleRequest;
import com.stephen.spring_boot_api.dto.response.PermissionResponse;
import com.stephen.spring_boot_api.dto.response.RoleResponse;
import com.stephen.spring_boot_api.entity.Role;
import com.stephen.spring_boot_api.repository.PermissionRepository;
import com.stephen.spring_boot_api.repository.RoleRepository;
import com.stephen.spring_boot_api.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;

    public RoleServiceImpl(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public RoleResponse create(RoleRequest request) {
        Role role = new Role();
        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        role.setPermissions(new HashSet<>(permissions));

        role = roleRepository.save(role);
        RoleResponse response = new RoleResponse();
        response.setName(role.getName());
        response.setDescription(role.getDescription());
        response.setPermissions(role.getPermissions().stream()
                .map(permission -> {
                    PermissionResponse permissionResponse = new PermissionResponse();
                    permissionResponse.setName(permission.getName());
                    permissionResponse.setDescription(permission.getDescription());
                    return permissionResponse;
                })
                .collect(Collectors.toSet()));

        return response;
    }

    public List<RoleResponse> getAll() {
        var roles = roleRepository.findAll();
        return roles.stream()
                .map(role -> {
                    RoleResponse response = new RoleResponse();
                    response.setName(role.getName());
                    response.setDescription(role.getDescription());
                    response.setPermissions(role.getPermissions().stream()
                            .map(permission -> {
                                PermissionResponse permissionResponse = new PermissionResponse();
                                permissionResponse.setName(permission.getName());
                                permissionResponse.setDescription(permission.getDescription());
                                return permissionResponse;
                            })
                            .collect(Collectors.toSet()));

                    return response;
                })
                .toList();
    }

    public void delete(String role) {
        roleRepository.deleteById(role);
    }
}
