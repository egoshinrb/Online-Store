package com.onlinestore.server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinestore.server.websocket.WebSocketSessionRegistry;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class RealtimeNotificationService {

    private static final Logger LOG = LoggerFactory.getLogger(RealtimeNotificationService.class);

    private final WebSocketSessionRegistry webSocketSessionRegistry;
    private final FcmNotificationService fcmNotificationService;
    private final ObjectMapper objectMapper;

    public RealtimeNotificationService(
            WebSocketSessionRegistry webSocketSessionRegistry,
            FcmNotificationService fcmNotificationService,
            ObjectMapper objectMapper
    ) {
        this.webSocketSessionRegistry = webSocketSessionRegistry;
        this.fcmNotificationService = fcmNotificationService;
        this.objectMapper = objectMapper;
    }

    public void notifyOrderStatus(Long userId, Long orderId, String status) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("orderId", orderId);
        payload.put("status", status);
        send(userId, "orders", payload);
    }

    public void notifyPromotion(Long userId, String title, String body) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("title", title);
        payload.put("body", body);
        send(userId, "promotions", payload);
    }

    public void broadcastPromotion(String title, String body) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("title", title);
        payload.put("body", body);
        String json = toJson("notification", "promotions", payload);
        webSocketSessionRegistry.broadcastToAll(json);
        fcmNotificationService.broadcastPromotion(title, body);
    }

    private void send(Long userId, String channel, Map<String, Object> payload) {
        String json = toJson("notification", channel, payload);
        webSocketSessionRegistry.broadcastToUser(userId, json);
        fcmNotificationService.sendToUser(userId, channel, payload);
    }

    private String toJson(String type, String channel, Map<String, Object> payload) {
        try {
            Map<String, Object> root = new HashMap<>();
            root.put("type", type);
            root.put("channel", channel);
            root.put("payload", payload);
            return objectMapper.writeValueAsString(root);
        } catch (JsonProcessingException e) {
            LOG.error("JSON error", e);
            return "{}";
        }
    }
}
