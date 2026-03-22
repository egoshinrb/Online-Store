package com.onlinestore.server.model.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Serdeable
public final class AddressDtos {

    private AddressDtos() {
    }

    @Introspected
    @Serdeable
    public record AddressRequest(
            @Size(max = 255) String label,
            @NotBlank String addressLine,
            Double latitude,
            Double longitude,
            boolean defaultAddress
    ) {
    }

    @Introspected
    @Serdeable
    public record AddressDto(
            Long id,
            String label,
            String addressLine,
            Double latitude,
            Double longitude,
            boolean defaultAddress
    ) {
    }
}
