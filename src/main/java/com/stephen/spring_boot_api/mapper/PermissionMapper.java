package com.stephen.spring_boot_api.mapper;

import org.mapstruct.Mapper;

import com.stephen.spring_boot_api.dto.request.PermissionRequest;
import com.stephen.spring_boot_api.dto.response.PermissionResponse;
import com.stephen.spring_boot_api.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
