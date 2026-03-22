package com.onlinestore.server.controller;

import com.onlinestore.server.model.dto.AuthDtos;
import com.onlinestore.server.service.AuthService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Controller("/api/auth")
@Tag(name = "Auth")
@Secured(SecurityRule.IS_ANONYMOUS)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Post("/register")
    @Operation(summary = "Регистрация")
    public HttpResponse<AuthDtos.TokenResponse> register(@Body @Valid AuthDtos.RegisterRequest req) {
        return HttpResponse.created(authService.register(req));
    }

    @Post("/login")
    @Operation(summary = "Вход")
    public HttpResponse<AuthDtos.TokenResponse> login(@Body @Valid AuthDtos.LoginRequest req) {
        return HttpResponse.ok(authService.login(req));
    }

    @Post("/refresh")
    @Operation(summary = "Обновление токенов")
    public HttpResponse<AuthDtos.TokenResponse> refresh(@Body @Valid AuthDtos.RefreshRequest req) {
        return HttpResponse.ok(authService.refresh(req));
    }

    @Post("/logout")
    @Operation(summary = "Выход (отзыв refresh-токена)")
    public HttpResponse<Void> logout(@Body @Valid AuthDtos.RefreshRequest req) {
        authService.logout(req.refreshToken());
        return HttpResponse.noContent();
    }
}
