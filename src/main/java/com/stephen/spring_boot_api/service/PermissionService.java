package com.stephen.spring_boot_api.service;

import java.util.List;

import com.stephen.spring_boot_api.dto.request.PermissionRequest;
import com.stephen.spring_boot_api.dto.response.PermissionResponse;

public interface PermissionService {
    PermissionResponse create(PermissionRequest request);

    List<PermissionResponse> getAll();

    void delete(String permission);
}
