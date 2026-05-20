package com.alligator.market.backend.process.quotemonitor.application.livequote;

import com.alligator.market.backend.process.quotemonitor.registry.runtime.AtomicRuntimeQuoteMonitorLiveQuoteRegistry;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.process.quotemonitor.livequote.QuoteMonitorLiveQuote;
import com.alligator.market.domain.source.vo.SourceCode;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Instant;

class QuoteMonitorLiveQuoteStreamServiceTest {
    private static final Instant SOURCE_TIME = Instant.parse("2026-05-18T08:00:00Z");
    private static final Instant RECEIVED_AT = Instant.parse("2026-05-18T08:00:01Z");

    @Test
    void streamsCurrentSnapshotAndFutureUpdates() {
        AtomicRuntimeQuoteMonitorLiveQuoteRegistry registry = new AtomicRuntimeQuoteMonitorLiveQuoteRegistry();
        QuoteMonitorLiveQuote snapshotQuote = quote("FOREX_SPOT_CNYRUB_TOM", "12.10");
        QuoteMonitorLiveQuote updateQuote = quote("FOREX_SPOT_USDRUB_TOM", "90.20");
        QuoteMonitorLiveQuoteStreamService service = new QuoteMonitorLiveQuoteStreamService(registry, registry);

        registry.publish(snapshotQuote);

        StepVerifier.create(service.streamQuotes())
                .expectNext(snapshotQuote)
                .then(() -> registry.publish(updateQuote))
                .expectNext(updateQuote)
                .thenCancel()
                .verify();
    }

    private static QuoteMonitorLiveQuote quote(String instrumentCode, String lastPrice) {
        return new QuoteMonitorLiveQuote(
                InstrumentCode.of(instrumentCode),
                SourceCode.of("MOEX_ISS"),
                new BigDecimal(lastPrice),
                SOURCE_TIME,
                RECEIVED_AT
        );
    }
}
