package com.alligator.market.backend.process.quotemonitor.application.quote;

import com.alligator.market.backend.process.quotemonitor.registry.runtime.AtomicRuntimeQuoteMonitorInstrumentQuoteRegistry;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.process.quotemonitor.quote.QuoteMonitorInstrumentQuote;
import com.alligator.market.domain.source.vo.SourceCode;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Instant;

class QuoteMonitorInstrumentQuoteStreamServiceTest {
    private static final Instant SOURCE_TICK_TIME = Instant.parse("2026-05-18T08:00:00Z");
    private static final Instant RECEIVED_AT = Instant.parse("2026-05-18T08:00:01Z");

    @Test
    void streamsCurrentSnapshotAndFutureUpdates() {
        AtomicRuntimeQuoteMonitorInstrumentQuoteRegistry registry = new AtomicRuntimeQuoteMonitorInstrumentQuoteRegistry();
        QuoteMonitorInstrumentQuote snapshotQuote = quote("FOREX_SPOT_CNYRUB_TOM", "12.10");
        QuoteMonitorInstrumentQuote updateQuote = quote("FOREX_SPOT_USDRUB_TOM", "90.20");
        QuoteMonitorInstrumentQuoteStreamService service =
                new QuoteMonitorInstrumentQuoteStreamService(registry, registry);

        registry.publish(snapshotQuote);

        StepVerifier.create(service.streamQuotes())
                .expectNext(snapshotQuote)
                .then(() -> registry.publish(updateQuote))
                .expectNext(updateQuote)
                .thenCancel()
                .verify();
    }

    private static QuoteMonitorInstrumentQuote quote(String instrumentCode, String lastPrice) {
        return new QuoteMonitorInstrumentQuote(
                InstrumentCode.of(instrumentCode),
                SourceCode.of("MOEX_ISS"),
                new BigDecimal(lastPrice),
                SOURCE_TICK_TIME,
                RECEIVED_AT
        );
    }
}
