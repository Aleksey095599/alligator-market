package com.alligator.market.backend.provider.catalog.web.dto;

/**
 * DTO дескриптора провайдера для REST-контроллера.
 */
public record ProviderDescriptorDto(
        String providerCode,
        String displayName,
        String deliveryMode,
        String accessMethod,
        boolean bulkSubscription,
        long minUpdateIntervalSeconds
) {
}
