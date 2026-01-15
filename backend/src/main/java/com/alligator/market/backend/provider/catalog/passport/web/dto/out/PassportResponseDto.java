package com.alligator.market.backend.provider.catalog.passport.web.dto.out;

/**
 * DTO для передачи паспорта провайдера (out).
 */
public record PassportResponseDto(
        String displayName,
        String deliveryMode,
        String accessMethod,
        boolean bulkSubscription
) {
}
