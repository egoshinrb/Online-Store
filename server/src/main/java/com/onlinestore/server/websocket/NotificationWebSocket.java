package com.onlinestore.server.websocket;

import com.onlinestore.server.security.JwtTokenService;
import io.micronaut.http.HttpRequest;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.OnClose;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import io.micronaut.websocket.annotation.ServerWebSocket;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@ServerWebSocket("/ws/notifications")
public class NotificationWebSocket {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationWebSocket.class);

    @Inject
    private JwtTokenService jwtTokenService;
    @Inject
    private WebSocketSessionRegistry registry;

    @OnOpen
    public void onOpen(WebSocketSession session, HttpRequest request) {
        Optional<String> fromQuery = request.getParameters().getFirst("access_token");
        String token = fromQuery.orElseGet(() -> extractBearer(request));
        if (token == null || token.isBlank()) {
            LOG.warn("WS rejected: no token");
            session.close();
            return;
        }
        jwtTokenService.parseUserId(token).ifPresentOrElse(
                userId -> registry.register(userId, session),
                () -> {
                    LOG.warn("WS rejected: invalid token");
                    session.close();
                }
        );
    }

    private static String extractBearer(HttpRequest request) {
        return request.getHeaders().findFirst("Authorization")
                .map(v -> v.startsWith("Bearer ") ? v.substring(7).trim() : v)
                .orElse(null);
    }

    @OnClose
    public void onClose(WebSocketSession session) {
        registry.unregister(session);
    }

    /**
     * Клиент может отправлять: {"type":"subscribe","channels":["orders","promotions"]}
     * (расширение: фильтрация по каналам на сервере).
     */
    @OnMessage
    public void onMessage(String message, WebSocketSession session) {
        LOG.debug("WS message: {}", message);
    }
}
