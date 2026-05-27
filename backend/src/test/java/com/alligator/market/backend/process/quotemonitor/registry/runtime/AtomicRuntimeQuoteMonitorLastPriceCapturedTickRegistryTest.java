package com.alligator.market.backend.process.quotemonitor.registry.runtime;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.tick.level.source.type.SourceLastPriceTick;
import com.alligator.market.domain.marketdata.tick.level.source.vo.SourceInstrumentCode;
import com.alligator.market.domain.process.quotemonitor.marketdata.tick.captured.QuoteMonitorLastPriceCapturedTick;
import com.alligator.market.domain.source.vo.SourceCode;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AtomicRuntimeQuoteMonitorLastPriceCapturedTickRegistryTest {
    private static final Instant SOURCE_TICK_TIME = Instant.parse("2026-05-18T08:00:00Z");
    private static final Instant RECEIVED_AT = Instant.parse("2026-05-18T08:00:01Z");

    @Test
    void returnsLatestPublishedTickByInstrumentCode() {
        InstrumentCode instrumentCode = InstrumentCode.of("FOREX_SPOT_USDRUB_TOM");
        AtomicRuntimeQuoteMonitorLastPriceCapturedTickRegistry registry =
                new AtomicRuntimeQuoteMonitorLastPriceCapturedTickRegistry();
        QuoteMonitorLastPriceCapturedTick firstTick = tick(instrumentCode, "90.10");
        QuoteMonitorLastPriceCapturedTick changedTick = tick(instrumentCode, "90.20");

        registry.publish(firstTick);
        registry.publish(changedTick);

        assertThat(registry.findByInstrumentCode(instrumentCode)).isEqualTo(Optional.of(changedTick));
        assertThat(registry.currentTicks()).containsExactly(changedTick);
    }

    @Test
    void clearRemovesCurrentTicks() {
        AtomicRuntimeQuoteMonitorLastPriceCapturedTickRegistry registry =
                new AtomicRuntimeQuoteMonitorLastPriceCapturedTickRegistry();
        QuoteMonitorLastPriceCapturedTick tick = tick(InstrumentCode.of("FOREX_SPOT_USDRUB_TOM"), "90.10");

        registry.publish(tick);
        registry.clear();

        assertThat(registry.findByInstrumentCode(tick.instrumentCode())).isEmpty();
        assertThat(registry.currentTicks()).isEmpty();
    }

    @Test
    void emitsPublishedTicksToLiveUpdateStream() {
        AtomicRuntimeQuoteMonitorLastPriceCapturedTickRegistry registry =
                new AtomicRuntimeQuoteMonitorLastPriceCapturedTickRegistry();
        QuoteMonitorLastPriceCapturedTick tick = tick(InstrumentCode.of("FOREX_SPOT_USDRUB_TOM"), "90.10");

        StepVerifier.create(registry.tickUpdates())
                .then(() -> registry.publish(tick))
                .expectNext(tick)
                .thenCancel()
                .verify();
    }

    private static QuoteMonitorLastPriceCapturedTick tick(InstrumentCode instrumentCode, String lastPrice) {
        return new QuoteMonitorLastPriceCapturedTick(
                instrumentCode,
                SourceCode.of("MOEX_ISS"),
                new SourceLastPriceTick(
                        SourceInstrumentCode.of(instrumentCode.value()),
                        new BigDecimal(lastPrice),
                        SOURCE_TICK_TIME
                ),
                RECEIVED_AT
        );
    }
}
