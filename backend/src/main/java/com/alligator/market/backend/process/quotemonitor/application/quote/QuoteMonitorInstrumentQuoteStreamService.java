package com.alligator.market.backend.process.quotemonitor.application.quote;

import com.alligator.market.domain.process.quotemonitor.quote.QuoteMonitorInstrumentQuote;
import com.alligator.market.domain.process.quotemonitor.quote.registry.runtime.RuntimeQuoteMonitorInstrumentQuoteRegistry;
import reactor.core.publisher.Flux;

import java.util.Objects;

public final class QuoteMonitorInstrumentQuoteStreamService {
    private final RuntimeQuoteMonitorInstrumentQuoteRegistry registry;
    private final QuoteMonitorInstrumentQuoteUpdateStream updateStream;

    public QuoteMonitorInstrumentQuoteStreamService(
            RuntimeQuoteMonitorInstrumentQuoteRegistry registry,
            QuoteMonitorInstrumentQuoteUpdateStream updateStream
    ) {
        this.registry = Objects.requireNonNull(registry, "registry must not be null");
        this.updateStream = Objects.requireNonNull(updateStream, "updateStream must not be null");
    }

    public Flux<QuoteMonitorInstrumentQuote> streamQuotes() {
        return Flux.defer(() -> Flux.concat(
                Flux.fromIterable(registry.currentQuotes()),
                updateStream.instrumentQuoteUpdates()
        ));
    }
}
