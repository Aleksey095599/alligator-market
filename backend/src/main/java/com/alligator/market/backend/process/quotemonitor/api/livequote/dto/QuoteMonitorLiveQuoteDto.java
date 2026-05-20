package com.alligator.market.backend.process.quotemonitor.api.livequote.dto;

import java.time.Instant;

public record QuoteMonitorLiveQuoteDto(
        String instrumentCode,
        String lastPrice,
        String sourceCode,
        Instant sourceTimestamp,
        Instant receivedAt
) {
}
