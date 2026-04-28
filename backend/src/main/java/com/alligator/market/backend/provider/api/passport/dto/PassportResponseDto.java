package com.alligator.market.backend.provider.api.passport.dto;

/**
 * DTO для передачи паспорта провайдера (out).
 */
public record PassportResponseDto(
        String providerCode,
        String displayName,
        String deliveryMode,
        String accessMethod,
        boolean bulkSubscription
) {
}
