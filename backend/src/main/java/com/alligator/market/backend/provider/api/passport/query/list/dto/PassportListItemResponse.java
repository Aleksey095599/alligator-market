package com.alligator.market.backend.provider.api.passport.query.list.dto;

/**
 * API-ответ для одного элемента из списка паспортов.
 */
public record PassportListItemResponse(
        String providerCode,
        String displayName,
        String deliveryMode,
        String accessMethod,
        boolean bulkSubscription
) {
}
