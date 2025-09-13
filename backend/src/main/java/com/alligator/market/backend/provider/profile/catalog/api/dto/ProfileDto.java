package com.alligator.market.backend.provider.profile.catalog.api.dto;

import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.provider.model.info.AccessMethod;
import com.alligator.market.domain.provider.model.info.DeliveryMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

/**
 * Основной DTO профиля провайдера.
 */
public record ProfileDto(

        @NotNull ProfileStatus profileStatus,
        @NotBlank String providerCode,
        @NotBlank String displayName,
        @NotNull Set<InstrumentType> instrumentsSupported,
        @NotNull DeliveryMode deliveryMode,
        @NotNull AccessMethod accessMethod,
        boolean bulkSubscription,
        int minPollMs
) {}
