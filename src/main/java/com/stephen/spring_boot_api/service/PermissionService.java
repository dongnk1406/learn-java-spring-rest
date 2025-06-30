package com.stephen.spring_boot_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.stephen.spring_boot_api.dto.request.PermissionRequest;
import com.stephen.spring_boot_api.dto.response.PermissionResponse;
import com.stephen.spring_boot_api.entity.Permission;
import com.stephen.spring_boot_api.repository.PermissionRepository;

@Service
public class PermissionService {
    PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public PermissionResponse create(PermissionRequest request) {
        Permission permission = new Permission();
        permission.setName(request.getName());
        permission.setDescription(request.getDescription());
        permission = permissionRepository.save(permission);
        PermissionResponse response = new PermissionResponse();
        response.setName(permission.getName());
        response.setDescription(permission.getDescription());
        return response;
    }

    public List<PermissionResponse> getAll() {
        var permissions = permissionRepository.findAll();
        // simple way to map using mapstruct
        return permissions.stream()
                .map(permission -> {
                    PermissionResponse response = new PermissionResponse();
                    response.setName(permission.getName());
                    response.setDescription(permission.getDescription());
                    return response;
                })
                .toList();
    }

    public void delete(String permission) {
        permissionRepository.deleteById(permission);
    }
}
