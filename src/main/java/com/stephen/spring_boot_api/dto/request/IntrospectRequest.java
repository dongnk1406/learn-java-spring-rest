package com.stephen.spring_boot_api.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IntrospectRequest {
    private String token;
}
