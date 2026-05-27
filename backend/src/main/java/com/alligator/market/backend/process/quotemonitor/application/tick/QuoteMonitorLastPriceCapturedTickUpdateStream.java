package com.alligator.market.backend.process.quotemonitor.application.tick;

import com.alligator.market.domain.process.quotemonitor.marketdata.tick.QuoteMonitorLastPriceCapturedTick;
import reactor.core.publisher.Flux;

public interface QuoteMonitorLastPriceCapturedTickUpdateStream {
    Flux<QuoteMonitorLastPriceCapturedTick> tickUpdates();
}
