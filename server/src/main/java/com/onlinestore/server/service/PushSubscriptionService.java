package com.onlinestore.server.service;

import com.onlinestore.server.model.dto.SubscriptionDtos;
import com.onlinestore.server.model.entity.PushSubscription;
import com.onlinestore.server.model.entity.User;
import com.onlinestore.server.repository.PushSubscriptionRepository;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

@Singleton
public class PushSubscriptionService {

    private final PushSubscriptionRepository pushSubscriptionRepository;

    public PushSubscriptionService(PushSubscriptionRepository pushSubscriptionRepository) {
        this.pushSubscriptionRepository = pushSubscriptionRepository;
    }

    @Transactional
    public void register(User user, SubscriptionDtos.FcmSubscriptionRequest req) {
        pushSubscriptionRepository.findByUser_IdAndFcmToken(user.getId(), req.fcmToken())
                .ifPresentOrElse(
                        existing -> {
                            existing.setDeviceId(req.deviceId());
                            pushSubscriptionRepository.save(existing);
                        },
                        () -> {
                            PushSubscription p = new PushSubscription();
                            p.setUser(user);
                            p.setFcmToken(req.fcmToken());
                            p.setDeviceId(req.deviceId());
                            pushSubscriptionRepository.save(p);
                        }
                );
    }
}
