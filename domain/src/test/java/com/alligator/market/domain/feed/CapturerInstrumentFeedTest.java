package com.alligator.market.domain.feed;

import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.marketdata.feed.CapturerInstrumentFeed;
import com.alligator.market.domain.marketdata.feed.vo.PrioritizedMarketDataSource;
import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.tick.level.source.SourceTick;
import com.alligator.market.domain.source.MarketDataSource;
import com.alligator.market.domain.source.passport.SourcePassport;
import com.alligator.market.domain.source.passport.vo.SourceDisplayName;
import com.alligator.market.domain.source.vo.SourceCode;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CapturerInstrumentFeedTest {

    @Test
    void createsFeedForCapturerInstrumentAndPrioritizedSources() {
        CapturerCode capturerCode = CapturerCode.of("TEST_CAPTURER");
        InstrumentCode instrumentCode = InstrumentCode.of("EUR_USD");
        PrioritizedMarketDataSource primarySource = prioritizedSource("PRIMARY_SOURCE", 0);
        PrioritizedMarketDataSource backupSource = prioritizedSource("BACKUP_SOURCE", 1);

        CapturerInstrumentFeed feed = new CapturerInstrumentFeed(
                capturerCode,
                instrumentCode,
                List.of(primarySource, backupSource)
        );

        assertEquals(capturerCode, feed.capturerCode());
        assertEquals(instrumentCode, feed.instrumentCode());
        assertEquals(List.of(primarySource, backupSource), feed.prioritizedSources());
    }

    @Test
    void rejectsEmptyPrioritizedSources() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new CapturerInstrumentFeed(
                        CapturerCode.of("TEST_CAPTURER"),
                        InstrumentCode.of("EUR_USD"),
                        List.of()
                )
        );
    }

    @Test
    void rejectsDuplicateSourceCodes() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new CapturerInstrumentFeed(
                        CapturerCode.of("TEST_CAPTURER"),
                        InstrumentCode.of("EUR_USD"),
                        List.of(
                                prioritizedSource("PRIMARY_SOURCE", 0),
                                prioritizedSource("PRIMARY_SOURCE", 1)
                        )
                )
        );
    }

    @Test
    void rejectsDuplicatePriorities() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new CapturerInstrumentFeed(
                        CapturerCode.of("TEST_CAPTURER"),
                        InstrumentCode.of("EUR_USD"),
                        List.of(
                                prioritizedSource("PRIMARY_SOURCE", 0),
                                prioritizedSource("BACKUP_SOURCE", 0)
                        )
                )
        );
    }

    private static PrioritizedMarketDataSource prioritizedSource(
            String sourceCode,
            int priority
    ) {
        return new PrioritizedMarketDataSource(
                new TestMarketDataSource(SourceCode.of(sourceCode)),
                priority
        );
    }

    private record TestMarketDataSource(SourceCode code) implements MarketDataSource {
        private TestMarketDataSource {
            if (code == null) {
                throw new IllegalArgumentException("code must not be null");
            }
        }

        @Override
        public SourcePassport passport() {
            return new SourcePassport(SourceDisplayName.of("Test Source"));
        }

        @Override
        public <I extends Instrument> Publisher<SourceTick> streamSourceTicks(I instrument) {
            throw new UnsupportedOperationException("Test source does not stream ticks");
        }
    }
}
