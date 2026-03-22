package com.onlinestore.server.controller;

import com.onlinestore.server.model.dto.CatalogDtos;
import com.onlinestore.server.service.CurrentUserService;
import com.onlinestore.server.service.FavoriteService;
import io.micronaut.http.HttpResponse;
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

import java.util.List;

@Controller("/api/favorites")
@Tag(name = "Favorites")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final CurrentUserService currentUserService;

    public FavoriteController(FavoriteService favoriteService, CurrentUserService currentUserService) {
        this.favoriteService = favoriteService;
        this.currentUserService = currentUserService;
    }

    @Get
    @Operation(summary = "Избранное")
    public List<CatalogDtos.ProductDto> list(Authentication authentication) {
        var user = currentUserService.requireUser(authentication);
        return favoriteService.list(user);
    }

    @Post("/{productId}")
    @Operation(summary = "Добавить в избранное")
    public HttpResponse<Void> add(Authentication authentication, @PathVariable Long productId) {
        var user = currentUserService.requireUser(authentication);
        favoriteService.add(user, productId);
        return HttpResponse.noContent();
    }

    @Delete("/{productId}")
    @Operation(summary = "Удалить из избранного")
    public HttpResponse<Void> remove(Authentication authentication, @PathVariable Long productId) {
        var user = currentUserService.requireUser(authentication);
        favoriteService.remove(user, productId);
        return HttpResponse.noContent();
    }
}
