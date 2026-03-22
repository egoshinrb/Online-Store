package com.onlinestore.server.service;

import com.onlinestore.server.model.dto.PaymentDtos;
import com.onlinestore.server.model.entity.PaymentStatus;
import com.onlinestore.server.model.entity.StoreOrder;
import com.onlinestore.server.model.entity.User;
import com.onlinestore.server.repository.StoreOrderRepository;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.util.UUID;

/**
 * Заглушка под Google Pay / карту: проверяет наличие токена и помечает заказ оплаченным.
 * Реальная интеграция — через Stripe / банк-эквайринг.
 */
@Singleton
public class PaymentService {

    private final StoreOrderRepository orderRepository;
    private final RealtimeNotificationService realtimeNotificationService;

    public PaymentService(
            StoreOrderRepository orderRepository,
            RealtimeNotificationService realtimeNotificationService
    ) {
        this.orderRepository = orderRepository;
        this.realtimeNotificationService = realtimeNotificationService;
    }

    @Transactional
    public PaymentDtos.PaymentConfirmResponse confirm(User user, PaymentDtos.PaymentConfirmRequest req) {
        StoreOrder order = orderRepository.findByIdAndUser_Id(req.orderId(), user.getId())
                .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        if (order.getPaymentStatus() == PaymentStatus.COMPLETED) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Order already paid");
        }
        if (req.paymentToken() == null || req.paymentToken().isBlank()) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, "paymentToken required");
        }
        order.setPaymentStatus(PaymentStatus.COMPLETED);
        orderRepository.save(order);
        realtimeNotificationService.notifyOrderStatus(user.getId(), order.getId(), order.getStatus().name());
        String tx = UUID.randomUUID().toString();
        return new PaymentDtos.PaymentConfirmResponse(true, "Payment accepted (stub)", tx);
    }
}
