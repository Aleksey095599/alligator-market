package com.alligator.market.domain.process.quotemonitor.livequote.registry.runtime;

import com.alligator.market.domain.process.quotemonitor.livequote.QuoteMonitorLiveQuote;

public interface RuntimeQuoteMonitorLiveQuotePublisher {

    void publish(QuoteMonitorLiveQuote quote);

    void clear();
}
