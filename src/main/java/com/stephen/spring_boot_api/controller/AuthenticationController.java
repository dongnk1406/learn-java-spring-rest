package com.stephen.spring_boot_api.controller;

import java.text.ParseException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.JOSEException;
import com.stephen.spring_boot_api.dto.ApiResponse;
import com.stephen.spring_boot_api.dto.request.AuthenticationRequest;
import com.stephen.spring_boot_api.dto.request.IntrospectRequest;
import com.stephen.spring_boot_api.dto.request.LogoutRequest;
import com.stephen.spring_boot_api.dto.request.RefreshTokenRequest;
import com.stephen.spring_boot_api.dto.response.AuthenticationResponse;
import com.stephen.spring_boot_api.dto.response.IntrospectResponse;
import com.stephen.spring_boot_api.service.AuthenticationService;

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
        var authenticatedResponse = authenticationService.authenticated(request);
        apiResponse.setData(authenticatedResponse);
        return apiResponse;
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request)
            throws JOSEException, ParseException {
        ApiResponse<IntrospectResponse> apiResponse = new ApiResponse<>();
        var introspectResponse = authenticationService.introspect(request);
        apiResponse.setData(introspectResponse);
        return apiResponse;
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws JOSEException, ParseException {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        authenticationService.logout(request);
        return apiResponse;
    }

    @PostMapping("/refreshToken")
    public ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request)
            throws JOSEException, ParseException {

        AuthenticationResponse authenticationResponse = authenticationService.refreshToken(request);
        ApiResponse<AuthenticationResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(authenticationResponse);
        return apiResponse;
    }

}
