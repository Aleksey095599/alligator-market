package com.alligator.market.backend.process.quotemonitor.api.quote.dto;

import java.util.List;

public record QuoteMonitorInstrumentQuoteListResponse(
        List<QuoteMonitorInstrumentQuoteDto> quotes
) {
}
