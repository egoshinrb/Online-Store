package com.onlinestore.server.controller;

import com.onlinestore.server.model.dto.AddressDtos;
import com.onlinestore.server.service.AddressService;
import com.onlinestore.server.service.CurrentUserService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
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

@Controller("/api/addresses")
@Tag(name = "Addresses")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class AddressController {

    private final AddressService addressService;
    private final CurrentUserService currentUserService;

    public AddressController(AddressService addressService, CurrentUserService currentUserService) {
        this.addressService = addressService;
        this.currentUserService = currentUserService;
    }

    @Get
    @Operation(summary = "Адреса пользователя")
    public List<AddressDtos.AddressDto> list(Authentication authentication) {
        var user = currentUserService.requireUser(authentication);
        return addressService.list(user);
    }

    @Post
    @Operation(summary = "Добавить адрес")
    public HttpResponse<AddressDtos.AddressDto> create(Authentication authentication, @Body @Valid AddressDtos.AddressRequest req) {
        var user = currentUserService.requireUser(authentication);
        return HttpResponse.created(addressService.create(user, req));
    }

    @Delete("/{id}")
    @Operation(summary = "Удалить адрес")
    public HttpResponse<Void> delete(Authentication authentication, @PathVariable Long id) {
        var user = currentUserService.requireUser(authentication);
        addressService.delete(user, id);
        return HttpResponse.noContent();
    }
}
