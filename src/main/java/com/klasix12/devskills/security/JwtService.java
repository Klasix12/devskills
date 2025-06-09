package com.klasix12.devskills.security;

import com.klasix12.devskills.service.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private final String SECRET;
    private final RedisService redisService;
    private int accessTokenExpiration = 5 * 60 * 1000; // 5 min
    private int refreshTokenExpiration = 60 * 60 * 24 * 7 * 1000; // 7 days

    public JwtService(@Value("${jwt.secret}") String SECRET, RedisService redisService) {
        this.SECRET = SECRET;
        this.redisService = redisService;
    }

    public String generateAccessToken(String username) {
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Token is null or empty");
        }
        return getClaims(token).getSubject();
    }

    public String extractId(String token) {
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Token is null or empty");
        }
        return getClaims(token).getId();
    }

    public Date extractExpiration(String token) {
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Token is null or empty");
        }
        return getClaims(token).getExpiration();
    }

    public boolean isTokenValid(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        return redisService.exists(getClaims(token).getId());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        if (userDetails == null) {
            return false;
        }
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }


    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
