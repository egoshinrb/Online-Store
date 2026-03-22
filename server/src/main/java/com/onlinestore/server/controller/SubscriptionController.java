package com.onlinestore.server.controller;

import com.onlinestore.server.model.dto.SubscriptionDtos;
import com.onlinestore.server.service.CurrentUserService;
import com.onlinestore.server.service.PushSubscriptionService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Controller("/api/subscriptions")
@Tag(name = "Push")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class SubscriptionController {

    private final PushSubscriptionService pushSubscriptionService;
    private final CurrentUserService currentUserService;

    public SubscriptionController(PushSubscriptionService pushSubscriptionService, CurrentUserService currentUserService) {
        this.pushSubscriptionService = pushSubscriptionService;
        this.currentUserService = currentUserService;
    }

    @Post
    @Operation(summary = "Регистрация FCM-токена")
    public HttpResponse<Void> subscribe(Authentication authentication, @Body @Valid SubscriptionDtos.FcmSubscriptionRequest req) {
        var user = currentUserService.requireUser(authentication);
        pushSubscriptionService.register(user, req);
        return HttpResponse.noContent();
    }
}
