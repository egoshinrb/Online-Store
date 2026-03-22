package com.onlinestore.server.service;

import com.onlinestore.server.model.dto.AuthDtos;
import com.onlinestore.server.model.entity.User;
import com.onlinestore.server.repository.UserRepository;
import com.onlinestore.server.security.JwtTokenService;
import com.onlinestore.server.security.RefreshTokenService;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Singleton
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenService jwtTokenService,
            RefreshTokenService refreshTokenService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public AuthDtos.TokenResponse register(AuthDtos.RegisterRequest req) {
        if (userRepository.existsByEmail(req.email().toLowerCase())) {
            throw new HttpStatusException(HttpStatus.CONFLICT, "Email already registered");
        }
        User user = new User();
        user.setEmail(req.email().toLowerCase());
        user.setPasswordHash(passwordEncoder.encode(req.password()));
        user.setName(req.name());
        userRepository.save(user);
        return buildTokens(user);
    }

    @Transactional
    public AuthDtos.TokenResponse login(AuthDtos.LoginRequest req) {
        User user = userRepository.findByEmail(req.email().toLowerCase())
                .orElseThrow(() -> new HttpStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
        if (!passwordEncoder.matches(req.password(), user.getPasswordHash())) {
            throw new HttpStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        return buildTokens(user);
    }

    @Transactional
    public AuthDtos.TokenResponse refresh(AuthDtos.RefreshRequest req) {
        User user = refreshTokenService.validateAndConsume(req.refreshToken())
                .orElseThrow(() -> new HttpStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token"));
        refreshTokenService.revoke(req.refreshToken());
        return buildTokens(user);
    }

    @Transactional
    public void logout(String refreshToken) {
        refreshTokenService.revoke(refreshToken);
    }

    @Transactional
    public void logoutAll(User user) {
        refreshTokenService.revokeAllForUser(user);
    }

    private AuthDtos.TokenResponse buildTokens(User user) {
        String access = jwtTokenService.generateAccessToken(user.getId(), user.getEmail());
        String refresh = refreshTokenService.createRefreshToken(user);
        return new AuthDtos.TokenResponse(
                access,
                refresh,
                "Bearer",
                jwtTokenService.getAccessTtlSeconds()
        );
    }
}
