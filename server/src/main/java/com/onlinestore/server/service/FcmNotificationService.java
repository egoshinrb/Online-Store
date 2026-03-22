package com.onlinestore.server.service;

import com.onlinestore.server.model.entity.PushSubscription;
import com.onlinestore.server.repository.PushSubscriptionRepository;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Заглушка FCM: при наличии {@code GOOGLE_APPLICATION_CREDENTIALS} можно инициализировать Firebase Admin SDK.
 * Сейчас логирует и не отправляет push без настроенного сервисного аккаунта.
 */
@Singleton
public class FcmNotificationService {

    private static final Logger LOG = LoggerFactory.getLogger(FcmNotificationService.class);

    private final PushSubscriptionRepository pushSubscriptionRepository;

    public FcmNotificationService(PushSubscriptionRepository pushSubscriptionRepository) {
        this.pushSubscriptionRepository = pushSubscriptionRepository;
    }

    public void sendToUser(Long userId, String channel, Map<String, Object> payload) {
        List<PushSubscription> subs = pushSubscriptionRepository.findByUser_Id(userId);
        if (subs.isEmpty()) {
            return;
        }
        LOG.debug("FCM: would send to {} tokens for user {} channel {} payload {}", subs.size(), userId, channel, payload);
        // FirebaseMessaging.getInstance().send(...) when credentials configured
    }

    public void broadcastPromotion(String title, String body) {
        LOG.info("FCM broadcast promotion (stub): {} — {}", title, body);
    }
}
