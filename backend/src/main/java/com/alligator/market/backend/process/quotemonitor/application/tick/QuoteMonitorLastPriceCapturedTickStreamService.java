package com.alligator.market.backend.process.quotemonitor.application.tick;

import com.alligator.market.domain.process.quotemonitor.marketdata.tick.QuoteMonitorLastPriceCapturedTick;
import com.alligator.market.domain.process.quotemonitor.marketdata.tick.registry.runtime.RuntimeQuoteMonitorLastPriceCapturedTickRegistry;
import reactor.core.publisher.Flux;

import java.util.Objects;

public final class QuoteMonitorLastPriceCapturedTickStreamService {
    private final RuntimeQuoteMonitorLastPriceCapturedTickRegistry registry;
    private final QuoteMonitorLastPriceCapturedTickUpdateStream updateStream;

    public QuoteMonitorLastPriceCapturedTickStreamService(
            RuntimeQuoteMonitorLastPriceCapturedTickRegistry registry,
            QuoteMonitorLastPriceCapturedTickUpdateStream updateStream
    ) {
        this.registry = Objects.requireNonNull(registry, "registry must not be null");
        this.updateStream = Objects.requireNonNull(updateStream, "updateStream must not be null");
    }

    public Flux<QuoteMonitorLastPriceCapturedTick> streamTicks() {
        return Flux.defer(() -> Flux.concat(
                Flux.fromIterable(registry.currentTicks()),
                updateStream.tickUpdates()
        ));
    }
}
