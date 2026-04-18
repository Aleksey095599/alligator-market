package com.alligator.market.backend.provider.passport.web.dto.out;

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
