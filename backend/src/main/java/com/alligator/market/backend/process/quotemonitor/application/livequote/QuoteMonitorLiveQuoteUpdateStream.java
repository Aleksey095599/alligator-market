package com.alligator.market.backend.process.quotemonitor.application.livequote;

import com.alligator.market.domain.process.quotemonitor.quote.QuoteMonitorInstrumentQuote;
import reactor.core.publisher.Flux;

public interface QuoteMonitorLiveQuoteUpdateStream {
    Flux<QuoteMonitorInstrumentQuote> liveQuoteUpdates();
}
