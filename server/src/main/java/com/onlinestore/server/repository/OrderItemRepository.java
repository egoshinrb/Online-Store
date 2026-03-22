package com.onlinestore.server.repository;

import com.onlinestore.server.model.entity.OrderItem;
import com.onlinestore.server.model.entity.OrderItem.OrderItemId;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {

    @Query("SELECT oi FROM OrderItem oi JOIN FETCH oi.product WHERE oi.orderId = :orderId")
    List<OrderItem> findByOrderId(Long orderId);
}
