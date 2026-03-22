package com.onlinestore.server.controller;

import com.onlinestore.server.model.dto.AuthDtos;
import com.onlinestore.server.service.CurrentUserService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller("/api/me")
@Tag(name = "Profile")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class UserController {

    private final CurrentUserService currentUserService;

    public UserController(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }

    @Get
    @Operation(summary = "Текущий пользователь")
    public AuthDtos.UserSummary me(Authentication authentication) {
        var user = currentUserService.requireUser(authentication);
        return new AuthDtos.UserSummary(user.getId(), user.getEmail(), user.getName());
    }
}
