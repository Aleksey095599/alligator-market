package com.alligator.market.backend.process.quotemonitor.registry.runtime;

import com.alligator.market.backend.process.quotemonitor.application.livequote.QuoteMonitorLiveQuoteUpdateStream;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.process.quotemonitor.livequote.QuoteMonitorLiveQuote;
import com.alligator.market.domain.process.quotemonitor.livequote.registry.runtime.RuntimeQuoteMonitorLiveQuotePublisher;
import com.alligator.market.domain.process.quotemonitor.livequote.registry.runtime.RuntimeQuoteMonitorLiveQuoteRegistry;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public final class AtomicRuntimeQuoteMonitorLiveQuoteRegistry
        implements RuntimeQuoteMonitorLiveQuoteRegistry,
        RuntimeQuoteMonitorLiveQuotePublisher,
        QuoteMonitorLiveQuoteUpdateStream {
    private final AtomicReference<Map<InstrumentCode, QuoteMonitorLiveQuote>> quotesByInstrumentCode =
            new AtomicReference<>(Map.of());
    private final Sinks.Many<QuoteMonitorLiveQuote> liveQuoteUpdates =
            Sinks.many().multicast().directBestEffort();

    @Override
    public Optional<QuoteMonitorLiveQuote> findByInstrumentCode(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        return Optional.ofNullable(quotesByInstrumentCode.get().get(instrumentCode));
    }

    @Override
    public List<QuoteMonitorLiveQuote> currentQuotes() {
        return quotesByInstrumentCode.get()
                .values()
                .stream()
                .sorted(Comparator.comparing(quote -> quote.instrumentCode().value()))
                .toList();
    }

    @Override
    public Map<InstrumentCode, QuoteMonitorLiveQuote> quotesByInstrumentCode() {
        return quotesByInstrumentCode.get();
    }

    @Override
    public void publish(QuoteMonitorLiveQuote quote) {
        QuoteMonitorLiveQuote quoteToPublish = Objects.requireNonNull(
                quote,
                "quote must not be null"
        );

        quotesByInstrumentCode.updateAndGet(current -> {
            Map<InstrumentCode, QuoteMonitorLiveQuote> updated = new LinkedHashMap<>(current);
            updated.put(quoteToPublish.instrumentCode(), quoteToPublish);
            return Map.copyOf(updated);
        });
        liveQuoteUpdates.tryEmitNext(quoteToPublish);
    }

    @Override
    public void clear() {
        quotesByInstrumentCode.set(Map.of());
    }

    @Override
    public Flux<QuoteMonitorLiveQuote> liveQuoteUpdates() {
        return liveQuoteUpdates.asFlux();
    }
}
