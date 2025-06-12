package com.klasix12.devskills.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtProvider {
    private final String SECRET;
    private final long accessTokenExpiration = 5 * 60 * 1000; // 5 min
    private final long refreshTokenExpiration = 60 * 60 * 24 * 7 * 1000; //

    public JwtProvider(@Value("${jwt.secret}") String SECRET) {
        this.SECRET = SECRET;
    }

    public String generateAccessToken(String username) {
        return generateToken(username, accessTokenExpiration);
    }

    public String generateRefreshToken(String username) {
        return generateToken(username, refreshTokenExpiration);
    }

    public String extractUsername(String token) {
        return parseToken(token).getSubject();
    }

    public String extractId(String token) {
        return parseToken(token).getId();
    }

    public Date extractExpiration(String token) {
        return parseToken(token).getExpiration();
    }

    public boolean isTokenExpired(String token) {
        return parseToken(token).getExpiration().before(new Date());
    }

    private String generateToken(String username, long expiration) {
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    private Claims parseToken(String token) {
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
