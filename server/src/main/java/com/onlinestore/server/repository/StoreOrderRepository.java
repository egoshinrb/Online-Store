package com.onlinestore.server.repository;

import com.onlinestore.server.model.entity.StoreOrder;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreOrderRepository extends JpaRepository<StoreOrder, Long> {

    List<StoreOrder> findByUser_IdOrderByCreatedAtDesc(Long userId);

    Optional<StoreOrder> findByIdAndUser_Id(Long id, Long userId);
}
