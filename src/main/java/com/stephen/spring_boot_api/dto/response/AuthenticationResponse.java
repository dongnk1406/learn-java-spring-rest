package com.stephen.spring_boot_api.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationResponse {
    private boolean authenticated;
    private String accessToken;
}
