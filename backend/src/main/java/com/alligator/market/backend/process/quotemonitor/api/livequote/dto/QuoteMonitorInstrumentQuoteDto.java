package com.alligator.market.backend.process.quotemonitor.api.livequote.dto;

import java.time.Instant;

public record QuoteMonitorInstrumentQuoteDto(
        String instrumentCode,
        String lastPrice,
        String sourceCode,
        Instant sourceTickTime,
        Instant receivedAt
) {
}
