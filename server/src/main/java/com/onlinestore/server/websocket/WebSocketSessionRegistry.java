package com.onlinestore.server.websocket;

import io.micronaut.websocket.WebSocketSession;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class WebSocketSessionRegistry {

    private static final Logger LOG = LoggerFactory.getLogger(WebSocketSessionRegistry.class);

    private final Map<Long, Set<WebSocketSession>> sessionsByUser = new ConcurrentHashMap<>();
    private final Map<String, Long> userBySessionId = new ConcurrentHashMap<>();

    public void register(Long userId, WebSocketSession session) {
        sessionsByUser.computeIfAbsent(userId, k -> ConcurrentHashMap.newKeySet()).add(session);
        userBySessionId.put(session.getId(), userId);
        LOG.debug("WS registered user {} session {}", userId, session.getId());
    }

    public void unregister(WebSocketSession session) {
        Long userId = userBySessionId.remove(session.getId());
        if (userId != null) {
            Set<WebSocketSession> set = sessionsByUser.get(userId);
            if (set != null) {
                set.remove(session);
                if (set.isEmpty()) {
                    sessionsByUser.remove(userId);
                }
            }
        }
    }

    public void broadcastToUser(Long userId, String json) {
        Set<WebSocketSession> set = sessionsByUser.get(userId);
        if (set == null) {
            return;
        }
        for (WebSocketSession s : set) {
            if (s.isOpen()) {
                s.sendSync(json);
            }
        }
    }

    public void broadcastToAll(String json) {
        sessionsByUser.values().forEach(set ->
                set.stream().filter(WebSocketSession::isOpen).forEach(s -> s.sendSync(json)));
    }
}
