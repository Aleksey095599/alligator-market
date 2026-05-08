package com.alligator.market.backend.capturer.passport.api.query.list.dto;

/**
 * API-ответ для одного элемента из списка паспортов процессов захвата.
 */
public record MarketDataCapturerPassportListItemResponse(
        String capturerCode,
        String displayName,
        String lifecycleStatus
) {
}
