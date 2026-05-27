package com.alligator.market.backend.process.quotemonitor.application.tick;

import com.alligator.market.domain.process.quotemonitor.marketdata.tick.captured.QuoteMonitorLastPriceCapturedTick;
import com.alligator.market.domain.process.quotemonitor.marketdata.tick.captured.registry.runtime.RuntimeQuoteMonitorLastPriceCapturedTickRegistry;

import java.util.List;
import java.util.Objects;

public final class QuoteMonitorLastPriceCapturedTickQueryService {
    private final RuntimeQuoteMonitorLastPriceCapturedTickRegistry registry;

    public QuoteMonitorLastPriceCapturedTickQueryService(RuntimeQuoteMonitorLastPriceCapturedTickRegistry registry) {
        this.registry = Objects.requireNonNull(registry, "registry must not be null");
    }

    public List<QuoteMonitorLastPriceCapturedTick> currentTicks() {
        return registry.currentTicks();
    }
}
