package com.alligator.market.backend.process.quotemonitor.api.instrument.dto;

import java.util.List;

public record QuoteMonitorInstrumentOptionsResponse(
        List<QuoteMonitorInstrumentOptionDto> options
) {
}
