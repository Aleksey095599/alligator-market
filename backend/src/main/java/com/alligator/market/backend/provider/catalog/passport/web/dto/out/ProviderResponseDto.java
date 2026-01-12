package com.alligator.market.backend.provider.catalog.passport.web.dto.out;

/**
 * DTO ответа для провайдеров (out).
 */
public record ProviderResponseDto(
        String providerCode,

        // Поля паспорта провайдера
        String displayName,
        String deliveryMode,
        String accessMethod,
        boolean bulkSubscription,

        // Поля политики провайдера
        long minUpdateIntervalSeconds
) {
}
