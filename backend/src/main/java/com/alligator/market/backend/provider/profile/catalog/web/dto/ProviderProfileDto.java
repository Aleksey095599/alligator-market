package com.alligator.market.backend.provider.profile.catalog.web.dto;

import com.alligator.market.domain.instrument.InstrumentType;
import com.alligator.market.domain.provider.profile.model.AccessMethod;
import com.alligator.market.domain.provider.profile.model.DeliveryMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

/**
 * DTO профиля провайдера.
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
