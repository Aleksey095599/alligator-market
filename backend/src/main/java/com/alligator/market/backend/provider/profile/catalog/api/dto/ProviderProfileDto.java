package com.alligator.market.backend.provider.profile.catalog.api.dto;

import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.provider.profile.model.ProviderAccessMethod;
import com.alligator.market.domain.provider.profile.model.ProviderDeliveryMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

/**
 * DTO профиля провайдера рыночных данных.
 */
public record ProviderProfileDto(

        @NotBlank String providerCode,
        @NotBlank String displayName,
        @NotNull Set<InstrumentType> instrumentsSupported,
        @NotNull ProviderDeliveryMode deliveryMode,
        @NotNull ProviderAccessMethod accessMethod,
        boolean bulkSubscription,
        int minPollMs
) {}
