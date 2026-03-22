package com.onlinestore.server.model.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Serdeable
public final class PaymentDtos {

    private PaymentDtos() {
    }

    /**
     * Запрос подтверждения оплаты (Google Pay / карта).
     * paymentToken — токен от платёжного провайдера (заглушка для интеграции).
     */
    @Introspected
    @Serdeable
    public record PaymentConfirmRequest(
            @NotNull Long orderId,
            @NotBlank String paymentToken,
            String provider
    ) {
    }

    @Introspected
    @Serdeable
    public record PaymentConfirmResponse(
            boolean success,
            String message,
            String transactionId
    ) {
    }
}
