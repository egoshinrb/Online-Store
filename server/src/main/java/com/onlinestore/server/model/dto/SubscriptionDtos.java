package com.onlinestore.server.model.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

@Serdeable
public final class SubscriptionDtos {

    private SubscriptionDtos() {
    }

    @Introspected
    @Serdeable
    public record FcmSubscriptionRequest(
            @NotBlank String fcmToken,
            String deviceId
    ) {
    }
}
