package com.alligator.market.backend.source.passport.api.query.list.dto;

/**
 * API-ответ для одного элемента из списка паспортов.
 */
public record PassportListItemResponse(
        String providerCode,
        String displayName,
        String deliveryMode,
        String accessMethod,
        boolean bulkSubscription,
        String lifecycleStatus
) {
}
