package com.stephen.spring_boot_api.dto.response;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleResponse {
    private String name;
    private String description;
    Set<PermissionResponse> permissions;
}
