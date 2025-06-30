package com.stephen.spring_boot_api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stephen.spring_boot_api.dto.ApiResponse;
import com.stephen.spring_boot_api.dto.request.PermissionRequest;
import com.stephen.spring_boot_api.dto.response.PermissionResponse;
import com.stephen.spring_boot_api.service.PermissionService;

@RestController
@RequestMapping("/permissions")
public class PermissionController {
    PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping()
    public ApiResponse<List<PermissionResponse>> getAll() {
        ApiResponse<List<PermissionResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(permissionService.getAll());
        return apiResponse;
    }

    @PostMapping()
    public ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {
        ApiResponse<PermissionResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(permissionService.create(request));
        return apiResponse;
    }

    @DeleteMapping("/{permission}")
    public ApiResponse<Void> delete(@PathVariable String permission) {
        permissionService.delete(permission);
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        return apiResponse;
    }
}
