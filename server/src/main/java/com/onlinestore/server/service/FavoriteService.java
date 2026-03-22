package com.onlinestore.server.service;

import com.onlinestore.server.model.dto.CatalogDtos;
import com.onlinestore.server.model.entity.Favorite;
import com.onlinestore.server.model.entity.User;
import com.onlinestore.server.repository.FavoriteRepository;
import com.onlinestore.server.repository.ProductRepository;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository;
    private final ProductCatalogService productCatalogService;

    public FavoriteService(
            FavoriteRepository favoriteRepository,
            ProductRepository productRepository,
            ProductCatalogService productCatalogService
    ) {
        this.favoriteRepository = favoriteRepository;
        this.productRepository = productRepository;
        this.productCatalogService = productCatalogService;
    }

    @Transactional
    public void add(User user, Long productId) {
        productRepository.findById(productId)
                .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        if (favoriteRepository.existsByUser_IdAndProductId(user.getId(), productId)) {
            return;
        }
        Favorite f = new Favorite();
        f.setUserId(user.getId());
        f.setProductId(productId);
        favoriteRepository.save(f);
    }

    @Transactional
    public void remove(User user, Long productId) {
        favoriteRepository.findByUser_IdAndProductId(user.getId(), productId)
                .ifPresent(favoriteRepository::delete);
    }

    @Transactional(readOnly = true)
    public List<CatalogDtos.ProductDto> list(User user) {
        List<Favorite> favs = favoriteRepository.findByUser_IdOrderByProductIdAsc(user.getId());
        return favs.stream()
                .map(Favorite::getProductId)
                .map(productId -> productCatalogService.product(productId, user.getId()))
                .collect(Collectors.toList());
    }
}
