package com.onlinestore.server.security;

import com.onlinestore.server.model.entity.RefreshToken;
import com.onlinestore.server.model.entity.User;
import com.onlinestore.server.repository.RefreshTokenRepository;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HexFormat;
import java.util.Optional;

@Singleton
public class RefreshTokenService {

    private static final int TOKEN_BYTES = 32;
    private static final int TTL_DAYS = 30;

    private final RefreshTokenRepository refreshTokenRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public String createRefreshToken(User user) {
        byte[] raw = new byte[TOKEN_BYTES];
        secureRandom.nextBytes(raw);
        String token = HexFormat.of().formatHex(raw);
        String hash = sha256Hex(token);

        RefreshToken entity = new RefreshToken();
        entity.setUser(user);
        entity.setTokenHash(hash);
        entity.setExpiresAt(Instant.now().plus(TTL_DAYS, ChronoUnit.DAYS));
        entity.setRevoked(false);
        refreshTokenRepository.save(entity);
        return token;
    }

    public Optional<User> validateAndConsume(String rawToken) {
        String hash = sha256Hex(rawToken);
        return refreshTokenRepository.findByTokenHash(hash)
                .filter(rt -> !rt.isRevoked())
                .filter(rt -> rt.getExpiresAt().isAfter(Instant.now()))
                .map(RefreshToken::getUser);
    }

    @Transactional
    public void revoke(String rawToken) {
        String hash = sha256Hex(rawToken);
        refreshTokenRepository.findByTokenHash(hash).ifPresent(rt -> {
            rt.setRevoked(true);
            refreshTokenRepository.save(rt);
        });
    }

    @Transactional
    public void revokeAllForUser(User user) {
        refreshTokenRepository.findByUser_Id(user.getId()).forEach(rt -> {
            rt.setRevoked(true);
            refreshTokenRepository.save(rt);
        });
    }

    private static String sha256Hex(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
