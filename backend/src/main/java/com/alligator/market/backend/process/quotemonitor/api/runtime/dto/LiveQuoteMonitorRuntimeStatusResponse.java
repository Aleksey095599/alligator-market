package com.alligator.market.backend.process.quotemonitor.api.runtime.dto;

import java.time.Instant;
import java.util.List;

public record LiveQuoteMonitorRuntimeStatusResponse(
        String status,
        List<String> monitoredInstrumentCodes,
        Instant lastTickAt
) {
}
