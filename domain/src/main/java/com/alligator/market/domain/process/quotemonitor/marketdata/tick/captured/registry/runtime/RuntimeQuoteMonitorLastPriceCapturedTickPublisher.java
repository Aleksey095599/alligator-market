package com.alligator.market.domain.process.quotemonitor.marketdata.tick.captured.registry.runtime;

import com.alligator.market.domain.process.quotemonitor.marketdata.tick.captured.QuoteMonitorLastPriceCapturedTick;

public interface RuntimeQuoteMonitorLastPriceCapturedTickPublisher {

    void publish(QuoteMonitorLastPriceCapturedTick tick);

    void clear();
}
