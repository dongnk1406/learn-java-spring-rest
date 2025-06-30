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
import com.stephen.spring_boot_api.dto.request.RoleRequest;
import com.stephen.spring_boot_api.dto.response.RoleResponse;
import com.stephen.spring_boot_api.service.RoleService;

@RestController
@RequestMapping("/roles")
public class RoleController {
    RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping()
    public ApiResponse<List<RoleResponse>> getAll() {
        ApiResponse<List<RoleResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(roleService.getAll());
        return apiResponse;
    }

    @PostMapping()
    public ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
        ApiResponse<RoleResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(roleService.create(request));
        return apiResponse;
    }

    @DeleteMapping("/{role}")
    public ApiResponse<Void> delete(@PathVariable String role) {
        roleService.delete(role);
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        return apiResponse;
    }
}
