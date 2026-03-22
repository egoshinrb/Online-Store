package com.onlinestore.server.controller;

import com.onlinestore.server.model.dto.CatalogDtos;
import com.onlinestore.server.service.CurrentUserService;
import com.onlinestore.server.service.ProductCatalogService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.math.BigDecimal;

@Controller("/api")
@Tag(name = "Catalog")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class CatalogController {

    private final ProductCatalogService productCatalogService;
    private final CurrentUserService currentUserService;

    public CatalogController(ProductCatalogService productCatalogService, CurrentUserService currentUserService) {
        this.productCatalogService = productCatalogService;
        this.currentUserService = currentUserService;
    }

    @Get("/categories")
    @Operation(summary = "Список категорий")
    public java.util.List<CatalogDtos.CategoryDto> categories() {
        return productCatalogService.categories();
    }

    @Get("/products")
    @Operation(summary = "Товары с фильтрами")
    public CatalogDtos.ProductListResponse products(
            Authentication authentication,
            @QueryValue @io.micronaut.core.annotation.Nullable Long categoryId,
            @QueryValue @io.micronaut.core.annotation.Nullable String q,
            @QueryValue @io.micronaut.core.annotation.Nullable BigDecimal minPrice,
            @QueryValue @io.micronaut.core.annotation.Nullable BigDecimal maxPrice,
            @QueryValue @io.micronaut.core.annotation.Nullable String brand,
            @QueryValue @io.micronaut.core.annotation.Nullable Boolean inStockOnly
    ) {
        var user = currentUserService.requireUser(authentication);
        CatalogDtos.ProductFilter filter = new CatalogDtos.ProductFilter(
                categoryId, q, minPrice, maxPrice, brand, inStockOnly
        );
        return productCatalogService.products(filter, user.getId());
    }

    @Get("/products/{id}")
    @Operation(summary = "Товар по id")
    public CatalogDtos.ProductDto product(Authentication authentication, @PathVariable Long id) {
        var user = currentUserService.requireUser(authentication);
        return productCatalogService.product(id, user.getId());
    }
}
