package com.alligator.market.backend.provider.api.passport.query.list.dto;

/**
 * DTO для передачи паспорта провайдера (out).
 */
public record PassportListItemResponse(
        String providerCode,
        String displayName,
        String deliveryMode,
        String accessMethod,
        boolean bulkSubscription
) {
}
