package com.klasix12.devskills.security;

import com.klasix12.devskills.service.RedisService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
@AllArgsConstructor
public class TokenManager {

    private final JwtProvider jwtProvider;
    private final RedisService redisService;

    public String generateAccessToken(String username) {
        return jwtProvider.generateAccessToken(username);
    }

    public String generateRefreshToken(String username) {
        String refreshToken = jwtProvider.generateRefreshToken(username);
        saveRefreshToken(refreshToken);
        return refreshToken;
    }

    public boolean isRefreshTokenValid(String refreshToken) {
        if (jwtProvider.isTokenExpired(refreshToken)) {
            return false;
        }
        return redisService.exists(jwtProvider.extractId(refreshToken));
    }

    public boolean isAccessTokenValid(String token, UserDetails userDetails) {
        String username = jwtProvider.extractUsername(token);
        return Objects.equals(username, userDetails.getUsername());
    }

    public boolean isTokenExpired(String token) {
        return jwtProvider.isTokenExpired(token);
    }

    public void revokeRefreshToken(String refreshToken) {
        redisService.delete(jwtProvider.extractId(refreshToken));
    }

    public String extractUsername(String token) {
        return jwtProvider.extractUsername(token);
    }

    private void saveRefreshToken(String refreshToken) {
        String tokenId = jwtProvider.extractId(refreshToken);
        String username = jwtProvider.extractUsername(refreshToken);
        Date expiration = jwtProvider.extractExpiration(refreshToken);
        redisService.save(tokenId, username, expiration.getTime() - System.currentTimeMillis());
    }
}
