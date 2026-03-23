package com.onlinestore.server.repository;

import com.onlinestore.server.model.entity.Category;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c ORDER BY c.sortOrder ASC")
    List<Category> findAllByOrderBySortOrderAsc();
}