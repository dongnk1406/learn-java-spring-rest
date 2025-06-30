package com.stephen.spring_boot_api.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.stephen.spring_boot_api.dto.request.AuthenticationRequest;
import com.stephen.spring_boot_api.dto.request.IntrospectRequest;
import com.stephen.spring_boot_api.dto.request.LogoutRequest;
import com.stephen.spring_boot_api.dto.request.RefreshTokenRequest;
import com.stephen.spring_boot_api.dto.response.AuthenticationResponse;
import com.stephen.spring_boot_api.dto.response.IntrospectResponse;
import com.stephen.spring_boot_api.entity.InvalidatedToken;
import com.stephen.spring_boot_api.entity.User;
import com.stephen.spring_boot_api.exception.AppException;
import com.stephen.spring_boot_api.exception.ErrorCode;
import com.stephen.spring_boot_api.repository.InvalidatedTokenRepository;
import com.stephen.spring_boot_api.repository.UserRepository;

@Service
public class AuthenticationService {
    private UserRepository userRepository;
    private InvalidatedTokenRepository invalidatedTokenRepository;

    // @Value is used to get value from application.properties
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    public AuthenticationService(UserRepository userRepository, InvalidatedTokenRepository invalidatedTokenRepository) {
        this.userRepository = userRepository;
        this.invalidatedTokenRepository = invalidatedTokenRepository;
    }

    public AuthenticationResponse authenticated(AuthenticationRequest request) {
        var user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        var authenticatedResponse = new AuthenticationResponse();
        var accessToken = generateAccessToken(user);
        authenticatedResponse.setAuthenticated(authenticated);
        authenticatedResponse.setAccessToken(accessToken);

        return authenticatedResponse;
    }

    public String generateAccessToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("com.stephen")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
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

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        var isValid = true;
        try {
            verifyToken(token, false);
        } catch (Exception e) {
            isValid = false;
        }

        var introspectResponse = new IntrospectResponse();
        introspectResponse.setValid(isValid);
        return introspectResponse;
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
                }
            });
        }
        return stringJoiner.toString();
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        boolean verified = signedJWT.verify(jwsVerifier);
        Date expiredTime = isRefresh
                ? new Date(signedJWT
                        .getJWTClaimsSet()
                        .getIssueTime()
                        .toInstant()
                        .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
                        .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean valid = verified && expiredTime.after(new Date());

        if (!valid) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return signedJWT;
    }

    public void logout(LogoutRequest request) throws JOSEException, ParseException {
        try {
            SignedJWT signedJWT = verifyToken(request.getToken(), true);
            String jwtTokenId = signedJWT.getJWTClaimsSet().getJWTID();
            Date jwtExpiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken = new InvalidatedToken();
            invalidatedToken.setId(jwtTokenId);
            invalidatedToken.setExpiryTime(jwtExpiryTime);
            invalidatedTokenRepository.save(invalidatedToken);
        } catch (AppException e) {
            System.out.println("Token already invalidated");
        }
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest request) throws JOSEException, ParseException {
        var signJwt = verifyToken(request.getToken(), true);
        String jwtTokenId = signJwt.getJWTClaimsSet().getJWTID();
        Date jwtExpiryTime = signJwt.getJWTClaimsSet().getExpirationTime();

        // invalidate token by adding to invalidatedToken table
        InvalidatedToken invalidatedToken = new InvalidatedToken();
        invalidatedToken.setId(jwtTokenId);
        invalidatedToken.setExpiryTime(jwtExpiryTime);
        invalidatedTokenRepository.save(invalidatedToken);

        // generate new access token for user
        String userName = signJwt.getJWTClaimsSet().getSubject();
        var user =
                userRepository.findByUsername(userName).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        String accessToken = generateAccessToken(user);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setAccessToken(accessToken);
        authenticationResponse.setAuthenticated(true);
        return authenticationResponse;
    }
}
