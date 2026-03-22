package com.onlinestore.server.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Singleton
public class JwtTokenService {

    private final SecretKey secretKey;
    private final long accessTtlMinutes;

    public JwtTokenService(
            @Value("${micronaut.security.token.jwt.signatures.secret.generator.secret}") String secret,
            @Value("${app.jwt.access-ttl-minutes:15}") long accessTtlMinutes
    ) {
        this.secretKey = buildKey(secret);
        this.accessTtlMinutes = accessTtlMinutes;
    }

    public long getAccessTtlSeconds() {
        return accessTtlMinutes * 60;
    }

    private static SecretKey buildKey(String secret) {
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) {
            try {
                bytes = MessageDigest.getInstance("SHA-256").digest(bytes);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        return Keys.hmacShaKeyFor(bytes);
    }

    public String generateAccessToken(Long userId, String email) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email", email)
                .claim("roles", List.of("ROLE_USER"))
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(accessTtlMinutes * 60)))
                .signWith(secretKey)
                .compact();
    }

    public Optional<Long> parseUserId(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return Optional.of(Long.parseLong(claims.getSubject()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
