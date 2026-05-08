package com.alligator.market.backend.source.passport.api.query.list.dto;

/**
 * API response item for a market data source passport.
 */
public record PassportListItemResponse(
        String sourceCode,
        String displayName,
        String deliveryMode,
        String accessMethod,
        boolean bulkSubscription,
        String lifecycleStatus
) {
}
