package com.onlinestore.server.repository;

import com.onlinestore.server.model.entity.Product;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryIdOrderByNameAsc(Long categoryId);
}
