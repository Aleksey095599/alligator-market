package com.alligator.market.backend.process.quotemonitor.application.quote;

import com.alligator.market.domain.process.quotemonitor.quote.QuoteMonitorInstrumentQuote;
import reactor.core.publisher.Flux;

public interface QuoteMonitorInstrumentQuoteUpdateStream {
    Flux<QuoteMonitorInstrumentQuote> instrumentQuoteUpdates();
}
