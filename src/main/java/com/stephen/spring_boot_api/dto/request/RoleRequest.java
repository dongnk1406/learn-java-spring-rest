package com.stephen.spring_boot_api.dto.request;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleRequest {
    private String name;
    private String description;
    Set<String> permissions;
}
