package com.stephen.spring_boot_api.service;

import java.util.List;

import com.stephen.spring_boot_api.dto.request.RoleRequest;
import com.stephen.spring_boot_api.dto.response.RoleResponse;

public interface RoleService {
    RoleResponse create(RoleRequest request);

    List<RoleResponse> getAll();

    void delete(String role);
}
