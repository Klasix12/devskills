package com.klasix12.devskills.service;

import com.klasix12.devskills.dto.AuthRequest;
import com.klasix12.devskills.dto.RefreshTokenRequest;
import com.klasix12.devskills.dto.TokenResponse;
import com.klasix12.devskills.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RedisService redisService;

    public TokenResponse login(AuthRequest req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String accessToken = jwtService.generateAccessToken(userDetails.getUsername());
        String refreshToken = jwtService.generateRefreshToken(userDetails.getUsername());
        saveRefreshToken(refreshToken);
        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse refresh(RefreshTokenRequest req) {
        if (!jwtService.isTokenValid(req.getRefreshToken())) {
            throw new RuntimeException("Invalid refresh token");
        }
        String username = jwtService.extractUsername(req.getRefreshToken());
        String newAccessToken = jwtService.generateAccessToken(username);
        String newRefreshToken = jwtService.generateRefreshToken(username);
        deleteRefreshToken(req.getRefreshToken());
        saveRefreshToken(newRefreshToken);
        return new TokenResponse(newAccessToken, newRefreshToken);
    }

    private void saveRefreshToken(String refreshToken) {
        String id = jwtService.extractId(refreshToken);
        String username = jwtService.extractUsername(refreshToken);
        long expiration = jwtService.extractExpiration(refreshToken).getTime() - System.currentTimeMillis();
        System.out.println(id + " | " + username + " | " + expiration);
        redisService.save(id, username, expiration);
    }

    private void deleteRefreshToken(String refreshToken) {
        redisService.delete(refreshToken);
    }
}
