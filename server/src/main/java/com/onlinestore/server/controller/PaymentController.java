package com.onlinestore.server.controller;

import com.onlinestore.server.model.dto.PaymentDtos;
import com.onlinestore.server.service.CurrentUserService;
import com.onlinestore.server.service.PaymentService;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Controller("/api/payments")
@Tag(name = "Payments")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class PaymentController {

    private final PaymentService paymentService;
    private final CurrentUserService currentUserService;

    public PaymentController(PaymentService paymentService, CurrentUserService currentUserService) {
        this.paymentService = paymentService;
        this.currentUserService = currentUserService;
    }

    @Post("/confirm")
    @Operation(summary = "Подтверждение оплаты (заглушка Google Pay / карта)")
    public PaymentDtos.PaymentConfirmResponse confirm(Authentication authentication, @Body @Valid PaymentDtos.PaymentConfirmRequest req) {
        var user = currentUserService.requireUser(authentication);
        return paymentService.confirm(user, req);
    }
}
