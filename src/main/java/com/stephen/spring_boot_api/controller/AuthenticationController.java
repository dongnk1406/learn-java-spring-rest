package com.stephen.spring_boot_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stephen.spring_boot_api.dto.ApiResponse;
import com.stephen.spring_boot_api.dto.request.AuthenticationRequest;
import com.stephen.spring_boot_api.dto.response.AuthenticationResponse;
import com.stephen.spring_boot_api.service.AuthenticationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        ApiResponse<AuthenticationResponse> apiResponse = new ApiResponse<>();
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setAuthenticated(authenticationService.authenticated(request));
        apiResponse.setData(authenticationResponse);
        return apiResponse;
    }

}
