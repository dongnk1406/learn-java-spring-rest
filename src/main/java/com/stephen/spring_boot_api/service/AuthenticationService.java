package com.stephen.spring_boot_api.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.stephen.spring_boot_api.dto.request.AuthenticationRequest;
import com.stephen.spring_boot_api.dto.response.AuthenticationResponse;
import com.stephen.spring_boot_api.exception.AppException;
import com.stephen.spring_boot_api.exception.ErrorCode;
import com.stephen.spring_boot_api.repository.UserRepository;

@Service
public class AuthenticationService {
    private UserRepository userRepository;
    protected static final String SIGNER_KEY = "2CNA6tIUosBKzREgym0LWBt9KbTFTbFuadSARDDyKo6i0RxrtDrj+s0dhaQ2b+7G";

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthenticationResponse authenticated(AuthenticationRequest request) {
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        var authenticatedResponse = new AuthenticationResponse();
        var accessToken = generateAccessToken(user.getUsername());
        authenticatedResponse.setAuthenticated(authenticated);
        authenticatedResponse.setAccessToken(accessToken);

        return authenticatedResponse;
    }

    private String generateAccessToken(String username) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder().subject(username).issuer("com.stephen")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .claim("customClaim", "customValue")
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
}
