package com.alligator.market.backend.process.quotemonitor.application.livequote;

import com.alligator.market.domain.process.quotemonitor.quote.QuoteMonitorInstrumentQuote;
import com.alligator.market.domain.process.quotemonitor.quote.registry.runtime.RuntimeQuoteMonitorLiveQuoteRegistry;
import reactor.core.publisher.Flux;

import java.util.Objects;

public final class QuoteMonitorLiveQuoteStreamService {
    private final RuntimeQuoteMonitorLiveQuoteRegistry registry;
    private final QuoteMonitorLiveQuoteUpdateStream updateStream;

    public QuoteMonitorLiveQuoteStreamService(
            RuntimeQuoteMonitorLiveQuoteRegistry registry,
            QuoteMonitorLiveQuoteUpdateStream updateStream
    ) {
        this.registry = Objects.requireNonNull(registry, "registry must not be null");
        this.updateStream = Objects.requireNonNull(updateStream, "updateStream must not be null");
    }

    public Flux<QuoteMonitorInstrumentQuote> streamQuotes() {
        return Flux.defer(() -> Flux.concat(
                Flux.fromIterable(registry.currentQuotes()),
                updateStream.liveQuoteUpdates()
        ));
    }
}
