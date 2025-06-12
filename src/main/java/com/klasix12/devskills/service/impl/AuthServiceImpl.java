package com.klasix12.devskills.service.impl;

import com.klasix12.devskills.dto.AuthRequest;
import com.klasix12.devskills.dto.RefreshTokenRequest;
import com.klasix12.devskills.dto.TokenResponse;
import com.klasix12.devskills.security.TokenManager;
import com.klasix12.devskills.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenManager tokenManager;

    public TokenResponse login(AuthRequest req) {
        log.info("Login attempt. Username: {}", req.getUsername());
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String accessToken = tokenManager.generateAccessToken(userDetails.getUsername());
        String refreshToken = tokenManager.generateRefreshToken(userDetails.getUsername());
        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse refresh(RefreshTokenRequest req) {
        if (!tokenManager.isRefreshTokenValid(req.getRefreshToken())) {
            throw new RuntimeException("Refresh token is invalid");
        }
        tokenManager.revokeRefreshToken(req.getRefreshToken());

        String username = tokenManager.extractUsername(req.getRefreshToken());
        String newAccessToken = tokenManager.generateAccessToken(username);
        String newRefreshToken = tokenManager.generateRefreshToken(username);
        log.info("Refresh tokens. Username: {}", username);
        return new TokenResponse(newAccessToken, newRefreshToken);
    }
}
