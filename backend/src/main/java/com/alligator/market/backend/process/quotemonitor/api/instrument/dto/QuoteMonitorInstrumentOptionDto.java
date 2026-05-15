package com.alligator.market.backend.process.quotemonitor.api.instrument.dto;

public record QuoteMonitorInstrumentOptionDto(
        String instrumentCode,
        boolean selected
) {
}
