package com.alligator.market.backend.capturer.passport.api.query.list.dto;

public record MarketDataCapturerPassportListItemResponse(
        String capturerCode,
        String displayName,
        String lifecycleStatus
) {
}
