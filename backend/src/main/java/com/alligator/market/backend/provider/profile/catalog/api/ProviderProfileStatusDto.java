package com.alligator.market.backend.provider.profile.catalog.api;

import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.provider.model.info.AccessMethod;
import com.alligator.market.domain.provider.model.info.DeliveryMode;

import java.util.Set;

/**
 * DTO профиля провайдера со статусом.
 */
public record ProviderProfileStatusDto(
        String providerCode,
        String displayName,
        Set<InstrumentType> instrumentsSupported,
        DeliveryMode deliveryMode,
        AccessMethod accessMethod,
        boolean bulkSubscription,
        int minPollMs,
        ProfileStatus status
) {}
