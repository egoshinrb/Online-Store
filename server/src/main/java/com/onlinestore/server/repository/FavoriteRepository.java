package com.onlinestore.server.repository;

import com.onlinestore.server.model.entity.Favorite;
import com.onlinestore.server.model.entity.Favorite.FavoriteId;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId> {

    List<Favorite> findByUser_IdOrderByProductIdAsc(Long userId);

    Optional<Favorite> findByUser_IdAndProductId(Long userId, Long productId);

    boolean existsByUser_IdAndProductId(Long userId, Long productId);
}
