package com.alligator.market.backend.marketdata.capture.process.passport.api.query.list.dto;

/**
 * API-ответ для одного элемента из списка паспортов процессов фиксации.
 */
public record MDCaptureProcessPassportListItemResponse(
        String captureProcessCode,
        String displayName
) {
}
