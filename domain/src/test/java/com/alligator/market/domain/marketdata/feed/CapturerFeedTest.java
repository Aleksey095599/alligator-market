package com.alligator.market.domain.marketdata.feed;

import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.feed.vo.PrioritizedSource;
import com.alligator.market.domain.marketdata.tick.level.source.SourceTick;
import com.alligator.market.domain.source.MarketDataSource;
import com.alligator.market.domain.source.passport.SourcePassport;
import com.alligator.market.domain.source.passport.vo.SourceDisplayName;
import com.alligator.market.domain.source.vo.SourceCode;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CapturerFeedTest {

    @Test
    void createsFeedForCapturerInstrumentAndPrioritizedSources() {
        CapturerCode capturerCode = CapturerCode.of("TEST_CAPTURER");
        InstrumentCode instrumentCode = InstrumentCode.of("EUR_USD");
        PrioritizedSource backupSource = prioritizedSource("BACKUP_SOURCE", 1);
        PrioritizedSource primarySource = prioritizedSource("PRIMARY_SOURCE", 0);

        CapturerFeed feed = new CapturerFeed(
                capturerCode,
                instrumentCode,
                List.of(backupSource, primarySource)
        );

        assertEquals(capturerCode, feed.capturerCode());
        assertEquals(instrumentCode, feed.instrumentCode());
        assertEquals(List.of(primarySource, backupSource), feed.prioritizedSources());
    }

    @Test
    void returnsNextSourceByPriority() {
        PrioritizedSource backupSource = prioritizedSource("BACKUP_SOURCE", 1);
        PrioritizedSource primarySource = prioritizedSource("PRIMARY_SOURCE", 0);

        CapturerFeed feed = feed(List.of(backupSource, primarySource));

        assertEquals(primarySource.source(), feed.nextSource());
    }

    @Test
    void returnsNextSourceAfterCurrentSource() {
        PrioritizedSource backupSource = prioritizedSource("BACKUP_SOURCE", 1);
        PrioritizedSource primarySource = prioritizedSource("PRIMARY_SOURCE", 0);

        CapturerFeed feed = feed(List.of(backupSource, primarySource));

        assertEquals(
                Optional.of(backupSource.source()),
                feed.nextSourceAfter(SourceCode.of("PRIMARY_SOURCE"))
        );
    }

    @Test
    void returnsEmptyNextSourceAfterLastSource() {
        PrioritizedSource backupSource = prioritizedSource("BACKUP_SOURCE", 1);
        PrioritizedSource primarySource = prioritizedSource("PRIMARY_SOURCE", 0);

        CapturerFeed feed = feed(List.of(backupSource, primarySource));

        assertEquals(Optional.empty(), feed.nextSourceAfter(SourceCode.of("BACKUP_SOURCE")));
    }

    @Test
    void rejectsEmptyPrioritizedSources() {
        assertThrows(
                IllegalArgumentException.class,
                () -> feed(List.of())
        );
    }

    @Test
    void rejectsDuplicateSourceCodes() {
        assertThrows(
                IllegalArgumentException.class,
                () -> feed(List.of(
                        prioritizedSource("PRIMARY_SOURCE", 0),
                        prioritizedSource("PRIMARY_SOURCE", 1)
                ))
        );
    }

    @Test
    void rejectsDuplicatePriorities() {
        assertThrows(
                IllegalArgumentException.class,
                () -> feed(List.of(
                        prioritizedSource("PRIMARY_SOURCE", 0),
                        prioritizedSource("BACKUP_SOURCE", 0)
                ))
        );
    }

    @Test
    void rejectsUnknownCurrentSourceWhenResolvingNextSource() {
        CapturerFeed feed = feed(List.of(prioritizedSource("PRIMARY_SOURCE", 0)));

        assertThrows(
                IllegalArgumentException.class,
                () -> feed.nextSourceAfter(SourceCode.of("UNKNOWN_SOURCE"))
        );
    }

    private static CapturerFeed feed(List<PrioritizedSource> prioritizedSources) {
        return new CapturerFeed(
                CapturerCode.of("TEST_CAPTURER"),
                InstrumentCode.of("EUR_USD"),
                prioritizedSources
        );
    }

    private static PrioritizedSource prioritizedSource(String sourceCode, int priority) {
        return new PrioritizedSource(
                new TestSource(SourceCode.of(sourceCode)),
                priority
        );
    }

    private record TestSource(SourceCode code) implements MarketDataSource {
        private TestSource {
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
