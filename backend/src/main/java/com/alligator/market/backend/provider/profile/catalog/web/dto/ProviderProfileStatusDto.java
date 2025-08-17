package com.alligator.market.backend.provider.profile.catalog.web.dto;

import com.alligator.market.domain.instrument.model.InstrumentType;
import com.alligator.market.domain.provider.profile.model.ProviderAccessMethod;
import com.alligator.market.domain.provider.profile.model.ProviderDeliveryMode;
import com.alligator.market.domain.provider.profile.context.ProviderProfileStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

/**
 * DTO профиля провайдера рыночных данных со статусом.
 */
public record ProviderProfileStatusDto(

        @NotNull ProviderProfileStatus status,
        @NotBlank String providerCode,
        @NotBlank String displayName,
        @NotNull Set<InstrumentType> instrumentTypes,
        @NotNull ProviderDeliveryMode deliveryMode,
        @NotNull ProviderAccessMethod accessMethod,
        boolean bulkSubscription,
        int minPollMs
) {}
