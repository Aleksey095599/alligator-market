package com.alligator.market.backend.process.quotemonitor.registry.runtime;

import com.alligator.market.backend.process.quotemonitor.application.tick.QuoteMonitorLastPriceCapturedTickUpdateStream;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.process.quotemonitor.marketdata.tick.captured.QuoteMonitorLastPriceCapturedTick;
import com.alligator.market.domain.process.quotemonitor.marketdata.tick.captured.registry.runtime.RuntimeQuoteMonitorLastPriceCapturedTickPublisher;
import com.alligator.market.domain.process.quotemonitor.marketdata.tick.captured.registry.runtime.RuntimeQuoteMonitorLastPriceCapturedTickRegistry;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public final class AtomicRuntimeQuoteMonitorLastPriceCapturedTickRegistry
        implements RuntimeQuoteMonitorLastPriceCapturedTickRegistry,
        RuntimeQuoteMonitorLastPriceCapturedTickPublisher,
        QuoteMonitorLastPriceCapturedTickUpdateStream {
    private final AtomicReference<Map<InstrumentCode, QuoteMonitorLastPriceCapturedTick>> ticksByInstrumentCode =
            new AtomicReference<>(Map.of());
    private final Sinks.Many<QuoteMonitorLastPriceCapturedTick> tickUpdates =
            Sinks.many().multicast().directBestEffort();

    @Override
    public Optional<QuoteMonitorLastPriceCapturedTick> findByInstrumentCode(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        return Optional.ofNullable(ticksByInstrumentCode.get().get(instrumentCode));
    }

    @Override
    public List<QuoteMonitorLastPriceCapturedTick> currentTicks() {
        return ticksByInstrumentCode.get()
                .values()
                .stream()
                .sorted(Comparator.comparing(tick -> tick.instrumentCode().value()))
                .toList();
    }

    @Override
    public Map<InstrumentCode, QuoteMonitorLastPriceCapturedTick> ticksByInstrumentCode() {
        return ticksByInstrumentCode.get();
    }

    @Override
    public void publish(QuoteMonitorLastPriceCapturedTick tick) {
        QuoteMonitorLastPriceCapturedTick tickToPublish = Objects.requireNonNull(
                tick,
                "tick must not be null"
        );

        ticksByInstrumentCode.updateAndGet(current -> {
            Map<InstrumentCode, QuoteMonitorLastPriceCapturedTick> updated = new LinkedHashMap<>(current);
            updated.put(tickToPublish.instrumentCode(), tickToPublish);
            return Map.copyOf(updated);
        });
        tickUpdates.tryEmitNext(tickToPublish);
    }

    @Override
    public void clear() {
        ticksByInstrumentCode.set(Map.of());
    }

    @Override
    public Flux<QuoteMonitorLastPriceCapturedTick> tickUpdates() {
        return tickUpdates.asFlux();
    }
}
