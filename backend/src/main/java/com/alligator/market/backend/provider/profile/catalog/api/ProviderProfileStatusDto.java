package com.alligator.market.backend.provider.profile.catalog.api;
import com.alligator.market.domain.provider.profile.model.AccessMethod;
import com.alligator.market.domain.provider.profile.model.DeliveryMode;
import com.alligator.market.domain.provider.profile.model.ProfileStatus;

import java.util.Set;

/**
 * DTO профиля провайдера со статусом.
 */
public record ProviderProfileStatusDto(
        String providerCode,
        String displayName,
        Set<String> instrumentsSupported,
        DeliveryMode deliveryMode,
        AccessMethod accessMethod,
        boolean bulkSubscription,
        int minPollMs,
        ProfileStatus status
) {}
