package com.onlinestore.server.repository;

import com.onlinestore.server.model.entity.UserAddress;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {

    List<UserAddress> findByUser_IdOrderByIdAsc(Long userId);

    void deleteByUser_IdAndId(Long userId, Long id);
}
