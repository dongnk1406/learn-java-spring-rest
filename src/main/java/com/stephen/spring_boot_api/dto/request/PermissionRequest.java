package com.stephen.spring_boot_api.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionRequest {
    private String name;
    private String description;
}
