package com.onlinestore.server.repository;

import com.onlinestore.server.model.entity.PushSubscription;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PushSubscriptionRepository extends JpaRepository<PushSubscription, Long> {

    List<PushSubscription> findByUser_Id(Long userId);

    Optional<PushSubscription> findByUser_IdAndFcmToken(Long userId, String fcmToken);

    void deleteByUser_IdAndFcmToken(Long userId, String fcmToken);
}
