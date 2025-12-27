package com.alligator.market.backend.provider.catalog.web.dto.out;

/**
 * DTO ответа для провайдеров (out).
 */
public record ProviderResponseDto(
        String providerCode,

        // Поля дескриптора провайдера
        String displayName,
        String deliveryMode,
        String accessMethod,
        boolean bulkSubscription,

        // Поля политики провайдера
        long minUpdateIntervalSeconds
) {
}
