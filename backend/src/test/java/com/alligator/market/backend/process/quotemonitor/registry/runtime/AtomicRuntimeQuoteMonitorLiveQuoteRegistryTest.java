package com.alligator.market.backend.process.quotemonitor.registry.runtime;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.process.quotemonitor.livequote.QuoteMonitorLiveQuote;
import com.alligator.market.domain.source.vo.SourceCode;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AtomicRuntimeQuoteMonitorLiveQuoteRegistryTest {
    private static final Instant SOURCE_TIME = Instant.parse("2026-05-18T08:00:00Z");
    private static final Instant RECEIVED_AT = Instant.parse("2026-05-18T08:00:01Z");

    @Test
    void returnsLatestPublishedQuoteByInstrumentCode() {
        InstrumentCode instrumentCode = InstrumentCode.of("FOREX_SPOT_USDRUB_TOM");
        AtomicRuntimeQuoteMonitorLiveQuoteRegistry registry = new AtomicRuntimeQuoteMonitorLiveQuoteRegistry();
        QuoteMonitorLiveQuote firstQuote = quote(instrumentCode, "90.10");
        QuoteMonitorLiveQuote changedQuote = quote(instrumentCode, "90.20");

        registry.publish(firstQuote);
        registry.publish(changedQuote);

        assertThat(registry.findByInstrumentCode(instrumentCode)).isEqualTo(Optional.of(changedQuote));
        assertThat(registry.currentQuotes()).containsExactly(changedQuote);
    }

    @Test
    void clearRemovesCurrentQuotes() {
        AtomicRuntimeQuoteMonitorLiveQuoteRegistry registry = new AtomicRuntimeQuoteMonitorLiveQuoteRegistry();
        QuoteMonitorLiveQuote quote = quote(InstrumentCode.of("FOREX_SPOT_USDRUB_TOM"), "90.10");

        registry.publish(quote);
        registry.clear();

        assertThat(registry.findByInstrumentCode(quote.instrumentCode())).isEmpty();
        assertThat(registry.currentQuotes()).isEmpty();
    }

    @Test
    void emitsPublishedQuotesToLiveUpdateStream() {
        AtomicRuntimeQuoteMonitorLiveQuoteRegistry registry = new AtomicRuntimeQuoteMonitorLiveQuoteRegistry();
        QuoteMonitorLiveQuote quote = quote(InstrumentCode.of("FOREX_SPOT_USDRUB_TOM"), "90.10");

        StepVerifier.create(registry.liveQuoteUpdates())
                .then(() -> registry.publish(quote))
                .expectNext(quote)
                .thenCancel()
                .verify();
    }

    private static QuoteMonitorLiveQuote quote(InstrumentCode instrumentCode, String lastPrice) {
        return new QuoteMonitorLiveQuote(
                instrumentCode,
                SourceCode.of("MOEX_ISS"),
                new BigDecimal(lastPrice),
                SOURCE_TIME,
                RECEIVED_AT
        );
    }
}
