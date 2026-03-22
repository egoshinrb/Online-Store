package com.onlinestore.server.repository;

import com.onlinestore.server.model.entity.RefreshToken;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenHash(String tokenHash);

    List<RefreshToken> findByUser_Id(Long userId);

    void deleteByExpiresAtBefore(Instant instant);
}
