package com.alligator.market.domain.process.quotemonitor.quote.registry.runtime;

import com.alligator.market.domain.process.quotemonitor.quote.QuoteMonitorInstrumentQuote;

public interface RuntimeQuoteMonitorLiveQuotePublisher {

    void publish(QuoteMonitorInstrumentQuote quote);

    void clear();
}
