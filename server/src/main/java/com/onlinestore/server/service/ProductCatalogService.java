package com.onlinestore.server.service;

import com.onlinestore.server.model.dto.CatalogDtos;
import com.onlinestore.server.model.entity.Product;
import com.onlinestore.server.repository.CategoryRepository;
import com.onlinestore.server.repository.FavoriteRepository;
import com.onlinestore.server.repository.ProductRepository;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Singleton
public class ProductCatalogService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final FavoriteRepository favoriteRepository;

    public ProductCatalogService(
            CategoryRepository categoryRepository,
            ProductRepository productRepository,
            FavoriteRepository favoriteRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional(readOnly = true)
    public List<CatalogDtos.CategoryDto> categories() {
        return categoryRepository.findAllByOrderBySortOrderAsc().stream()
                .map(c -> new CatalogDtos.CategoryDto(
                        c.getId(),
                        c.getName(),
                        c.getParent() != null ? c.getParent().getId() : null,
                        c.getSortOrder()
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CatalogDtos.ProductListResponse products(CatalogDtos.ProductFilter filter, Long userId) {
        List<Product> all = productRepository.findAll();
        if (filter != null && filter.categoryId() != null) {
            all = all.stream().filter(p -> p.getCategory().getId().equals(filter.categoryId())).collect(Collectors.toList());
        }
        if (filter != null && filter.q() != null && !filter.q().isBlank()) {
            String q = filter.q().toLowerCase(Locale.ROOT);
            all = all.stream()
                    .filter(p -> p.getName().toLowerCase(Locale.ROOT).contains(q)
                            || (p.getDescription() != null && p.getDescription().toLowerCase(Locale.ROOT).contains(q)))
                    .collect(Collectors.toList());
        }
        if (filter != null && filter.minPrice() != null) {
            all = all.stream().filter(p -> p.getPrice().compareTo(filter.minPrice()) >= 0).collect(Collectors.toList());
        }
        if (filter != null && filter.maxPrice() != null) {
            all = all.stream().filter(p -> p.getPrice().compareTo(filter.maxPrice()) <= 0).collect(Collectors.toList());
        }
        if (filter != null && filter.brand() != null && !filter.brand().isBlank()) {
            String b = filter.brand().toLowerCase(Locale.ROOT);
            all = all.stream()
                    .filter(p -> p.getBrand() != null && p.getBrand().toLowerCase(Locale.ROOT).equals(b))
                    .collect(Collectors.toList());
        }
        if (filter != null && Boolean.TRUE.equals(filter.inStockOnly())) {
            all = all.stream().filter(p -> p.getStock() > 0).collect(Collectors.toList());
        }
        all.sort(Comparator.comparing(Product::getName));
        List<CatalogDtos.ProductDto> dtos = all.stream()
                .map(p -> toDto(p, userId))
                .collect(Collectors.toList());
        return new CatalogDtos.ProductListResponse(dtos, dtos.size());
    }

    @Transactional(readOnly = true)
    public CatalogDtos.ProductDto product(Long id, Long userId) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        return toDto(p, userId);
    }

    private CatalogDtos.ProductDto toDto(Product p, Long userId) {
        boolean fav = userId != null && favoriteRepository.existsByUser_IdAndProductId(userId, p.getId());
        return new CatalogDtos.ProductDto(
                p.getId(),
                p.getCategory().getId(),
                p.getName(),
                p.getDescription(),
                p.getPrice(),
                p.getUnit(),
                p.getImageUrl(),
                p.getStock(),
                p.getBrand(),
                fav
        );
    }
}
