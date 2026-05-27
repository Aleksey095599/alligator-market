package com.alligator.market.backend.process.quotemonitor.application.tick;

import com.alligator.market.backend.process.quotemonitor.registry.runtime.AtomicRuntimeQuoteMonitorLastPriceCapturedTickRegistry;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.tick.level.source.type.SourceLastPriceTick;
import com.alligator.market.domain.marketdata.tick.level.source.vo.SourceInstrumentCode;
import com.alligator.market.domain.process.quotemonitor.marketdata.tick.captured.QuoteMonitorLastPriceCapturedTick;
import com.alligator.market.domain.source.vo.SourceCode;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Instant;

class QuoteMonitorLastPriceCapturedTickStreamServiceTest {
    private static final Instant SOURCE_TICK_TIME = Instant.parse("2026-05-18T08:00:00Z");
    private static final Instant RECEIVED_AT = Instant.parse("2026-05-18T08:00:01Z");

    @Test
    void streamsCurrentSnapshotAndFutureUpdates() {
        AtomicRuntimeQuoteMonitorLastPriceCapturedTickRegistry registry =
                new AtomicRuntimeQuoteMonitorLastPriceCapturedTickRegistry();
        QuoteMonitorLastPriceCapturedTick snapshotTick = tick("FOREX_SPOT_CNYRUB_TOM", "12.10");
        QuoteMonitorLastPriceCapturedTick updateTick = tick("FOREX_SPOT_USDRUB_TOM", "90.20");
        QuoteMonitorLastPriceCapturedTickStreamService service =
                new QuoteMonitorLastPriceCapturedTickStreamService(registry, registry);

        registry.publish(snapshotTick);

        StepVerifier.create(service.streamTicks())
                .expectNext(snapshotTick)
                .then(() -> registry.publish(updateTick))
                .expectNext(updateTick)
                .thenCancel()
                .verify();
    }

    private static QuoteMonitorLastPriceCapturedTick tick(String instrumentCode, String lastPrice) {
        InstrumentCode code = InstrumentCode.of(instrumentCode);
        return new QuoteMonitorLastPriceCapturedTick(
                code,
                SourceCode.of("MOEX_ISS"),
                new SourceLastPriceTick(
                        SourceInstrumentCode.of(code.value()),
                        new BigDecimal(lastPrice),
                        SOURCE_TICK_TIME
                ),
                RECEIVED_AT
        );
    }
}
