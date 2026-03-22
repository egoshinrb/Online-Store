package com.onlinestore.server.model.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;
import java.util.List;

@Serdeable
public final class CatalogDtos {

    private CatalogDtos() {
    }

    @Introspected
    @Serdeable
    public record CategoryDto(
            Long id,
            String name,
            Long parentId,
            int sortOrder
    ) {
    }

    @Introspected
    @Serdeable
    public record ProductDto(
            Long id,
            Long categoryId,
            String name,
            String description,
            BigDecimal price,
            String unit,
            String imageUrl,
            int stock,
            String brand,
            boolean favorite
    ) {
    }

    @Introspected
    @Serdeable
    public record ProductFilter(
            Long categoryId,
            String q,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String brand,
            Boolean inStockOnly
    ) {
    }

    @Introspected
    @Serdeable
    public record ProductListResponse(
            List<ProductDto> items,
            int total
    ) {
    }
}
