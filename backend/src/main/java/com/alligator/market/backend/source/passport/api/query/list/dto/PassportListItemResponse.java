package com.alligator.market.backend.source.passport.api.query.list.dto;

public record PassportListItemResponse(
        String sourceCode,
        String displayName,
        String deliveryMode,
        String accessMethod,
        boolean bulkSubscription,
        String lifecycleStatus
) {
}
