package com.alligator.market.backend.process.quotemonitor.api.livequote.dto;

import java.util.List;

public record QuoteMonitorInstrumentQuoteListResponse(
        List<QuoteMonitorInstrumentQuoteDto> quotes
) {
}
