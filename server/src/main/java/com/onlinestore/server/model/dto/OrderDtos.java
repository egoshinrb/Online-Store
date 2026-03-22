package com.onlinestore.server.model.dto;

import com.onlinestore.server.model.entity.OrderStatus;
import com.onlinestore.server.model.entity.PaymentMethod;
import com.onlinestore.server.model.entity.PaymentStatus;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Serdeable
public final class OrderDtos {

    private OrderDtos() {
    }

    @Introspected
    @Serdeable
    public record OrderItemRequest(
            @NotNull Long productId,
            @NotNull @Min(1) Integer quantity
    ) {
    }

    @Introspected
    @Serdeable
    public record CreateOrderRequest(
            @NotEmpty @Valid List<OrderItemRequest> items,
            @NotNull PaymentMethod paymentMethod,
            Long deliveryAddressId,
            String addressSnapshot
    ) {
    }

    @Introspected
    @Serdeable
    public record OrderItemDto(
            Long productId,
            String productName,
            int quantity,
            BigDecimal price
    ) {
    }

    @Introspected
    @Serdeable
    public record OrderDto(
            Long id,
            OrderStatus status,
            BigDecimal total,
            String addressSnapshot,
            Long deliveryAddressId,
            PaymentMethod paymentMethod,
            PaymentStatus paymentStatus,
            Instant createdAt,
            List<OrderItemDto> items
    ) {
    }
}
