package com.alligator.market.backend.provider.profile.catalog.web.dto;

import com.alligator.market.domain.instrument.InstrumentType;
import com.alligator.market.domain.provider.profile.AccessMethod;
import com.alligator.market.domain.provider.profile.DeliveryMode;
import com.alligator.market.domain.provider.profile.ProviderProfileStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

/**
 * DTO с информацией о статусе профиля провайдера.
 */
public record ProviderProfileStatusDto(
        @NotBlank String providerCode,
        @NotBlank String displayName,
        @NotNull Set<InstrumentType> instrumentTypes,
        @NotNull DeliveryMode deliveryMode,
        @NotNull AccessMethod accessMethod,
        boolean supportsBulkSubscription,
        int minPollPeriodMs,
        @NotNull ProviderProfileStatus status
) {}
