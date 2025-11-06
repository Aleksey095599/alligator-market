package com.alligator.market.backend.provider.catalog.web.dto;

/**
 * DTO дескриптора провайдера для REST-контроллера.
 */
public record ProviderDto(
        String displayName,
        String deliveryMode,
        String accessMethod,
        boolean bulkSubscription,
        long minUpdateIntervalSeconds
) {
}
