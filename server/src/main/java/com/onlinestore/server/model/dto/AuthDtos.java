package com.onlinestore.server.model.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Serdeable
public final class AuthDtos {

    private AuthDtos() {
    }

    @Introspected
    @Serdeable
    public record RegisterRequest(
            @NotBlank @Email String email,
            @NotBlank @Size(min = 8, max = 128) String password,
            @NotBlank @Size(max = 255) String name
    ) {
    }

    @Introspected
    @Serdeable
    public record LoginRequest(
            @NotBlank @Email String email,
            @NotBlank String password
    ) {
    }

    @Introspected
    @Serdeable
    public record RefreshRequest(
            @NotBlank String refreshToken
    ) {
    }

    @Introspected
    @Serdeable
    public record TokenResponse(
            String accessToken,
            String refreshToken,
            String tokenType,
            long expiresInSeconds
    ) {
    }

    @Introspected
    @Serdeable
    public record UserSummary(
            Long id,
            String email,
            String name
    ) {
    }
}
