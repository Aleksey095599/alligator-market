package com.alligator.market.backend.provider.profile.dto;

import com.alligator.market.domain.instrument.InstrumentType;
import com.alligator.market.domain.provider.profile.AccessMethod;
import com.alligator.market.domain.provider.profile.DeliveryMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

/**
 * DTO для информативного представления профиля провайдера.
 */
public record ProviderProfileDto(
        @NotBlank String providerCode,
        @NotBlank String displayName,
        @NotNull Set<InstrumentType> instrumentTypes,
        @NotNull DeliveryMode deliveryMode,
        @NotNull AccessMethod accessMethod,
        boolean supportsBulkSubscription,
        int minPollPeriodMs
) {}
