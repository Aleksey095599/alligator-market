package com.alligator.market.backend.process.quotemonitor.api.runtime.dto;

public record QuoteMonitorInstrumentRuntimeStateResponse(
        String instrumentCode,
        String sourceCode,
        String status,
        String detail
) {
}
