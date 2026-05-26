package com.alligator.market.domain.process.quotemonitor.quote.registry.runtime;

import com.alligator.market.domain.process.quotemonitor.quote.QuoteMonitorInstrumentQuote;

public interface RuntimeQuoteMonitorInstrumentQuotePublisher {

    void publish(QuoteMonitorInstrumentQuote quote);

    void clear();
}
