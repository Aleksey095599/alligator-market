package com.alligator.market.domain.process.quotemonitor.marketdata.tick.registry.runtime;

import com.alligator.market.domain.process.quotemonitor.marketdata.tick.QuoteMonitorLastPriceCapturedTick;

public interface RuntimeQuoteMonitorLastPriceCapturedTickPublisher {

    void publish(QuoteMonitorLastPriceCapturedTick tick);

    void clear();
}
