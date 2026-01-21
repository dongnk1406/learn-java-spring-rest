package com.stephen.spring_boot_api.service;

import java.text.ParseException;

import com.nimbusds.jose.JOSEException;
import com.stephen.spring_boot_api.dto.request.AuthenticationRequest;
import com.stephen.spring_boot_api.dto.request.IntrospectRequest;
import com.stephen.spring_boot_api.dto.request.LogoutRequest;
import com.stephen.spring_boot_api.dto.request.RefreshTokenRequest;
import com.stephen.spring_boot_api.dto.response.AuthenticationResponse;
import com.stephen.spring_boot_api.dto.response.IntrospectResponse;
import com.stephen.spring_boot_api.entity.User;

public interface AuthenticationService {
    AuthenticationResponse authenticated(AuthenticationRequest request);

    String generateAccessToken(User user);

    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;

    void logout(LogoutRequest request) throws JOSEException, ParseException;

    AuthenticationResponse refreshToken(RefreshTokenRequest request) throws JOSEException, ParseException;
}
