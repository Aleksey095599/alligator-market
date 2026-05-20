package com.alligator.market.backend.process.quotemonitor.api.livequote.dto;

import java.util.List;

public record QuoteMonitorLiveQuoteListResponse(
        List<QuoteMonitorLiveQuoteDto> quotes
) {
}
