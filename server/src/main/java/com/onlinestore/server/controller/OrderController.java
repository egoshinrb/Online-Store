package com.onlinestore.server.controller;

import com.onlinestore.server.model.dto.OrderDtos;
import com.onlinestore.server.service.CurrentUserService;
import com.onlinestore.server.service.OrderService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;

@Controller("/api/orders")
@Tag(name = "Orders")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class OrderController {

    private final OrderService orderService;
    private final CurrentUserService currentUserService;

    public OrderController(OrderService orderService, CurrentUserService currentUserService) {
        this.orderService = orderService;
        this.currentUserService = currentUserService;
    }

    @Post
    @Operation(summary = "Создать заказ")
    public HttpResponse<OrderDtos.OrderDto> create(Authentication authentication, @Body @Valid OrderDtos.CreateOrderRequest req) {
        var user = currentUserService.requireUser(authentication);
        return HttpResponse.created(orderService.create(user, req));
    }

    @Get
    @Operation(summary = "История заказов")
    public List<OrderDtos.OrderDto> list(Authentication authentication) {
        var user = currentUserService.requireUser(authentication);
        return orderService.list(user);
    }

    @Get("/{id}")
    @Operation(summary = "Заказ по id")
    public OrderDtos.OrderDto get(Authentication authentication, @PathVariable Long id) {
        var user = currentUserService.requireUser(authentication);
        return orderService.get(user, id);
    }
}
