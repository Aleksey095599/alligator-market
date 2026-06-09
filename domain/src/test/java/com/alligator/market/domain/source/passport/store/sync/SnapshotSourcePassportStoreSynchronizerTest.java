package com.alligator.market.domain.source.passport.store.sync;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.marketdata.tick.level.source.SourceTick;
import com.alligator.market.domain.source.MarketDataSource;
import com.alligator.market.domain.source.handler.InstrumentHandler;
import com.alligator.market.domain.source.passport.SourcePassport;
import com.alligator.market.domain.source.passport.store.SourcePassportRecord;
import com.alligator.market.domain.source.passport.store.SourcePassportStore;
import com.alligator.market.domain.source.passport.vo.SourceDisplayName;
import com.alligator.market.domain.source.registry.RuntimeSourceRegistry;
import com.alligator.market.domain.source.registry.SnapshotRuntimeSourceRegistry;
import com.alligator.market.domain.source.vo.SourceCode;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SnapshotSourcePassportStoreSynchronizerTest {

    @Test
    void synchronizesStoreFromSourceRegistrySnapshot() {
        SourceCode code = SourceCode.of("TEST_SOURCE");
        SourcePassport passport = passport("Test Source");
        RuntimeSourceRegistry sourceRegistry = new SnapshotRuntimeSourceRegistry(List.of(
                new TestSource(code, passport)
        ));
        CapturingSourcePassportStore passportStore = new CapturingSourcePassportStore();
        SourcePassportStoreSynchronizer synchronizer = new SnapshotSourcePassportStoreSynchronizer(
                sourceRegistry,
                passportStore
        );

        synchronizer.synchronizeStoreFromSourceRegistry();

        assertEquals(Set.of(code), passportStore.retiredAllExcept);
        assertEquals(
                List.of(SourcePassportRecord.registered(code, passport)),
                passportStore.savedRecords
        );
    }

    @Test
    void rejectsDuplicatePassportDisplayNames() {
        RuntimeSourceRegistry sourceRegistry = new SnapshotRuntimeSourceRegistry(List.of(
                new TestSource(SourceCode.of("FIRST_SOURCE"), passport("Duplicate")),
                new TestSource(SourceCode.of("SECOND_SOURCE"), passport("duplicate"))
        ));
        SourcePassportStoreSynchronizer synchronizer = new SnapshotSourcePassportStoreSynchronizer(
                sourceRegistry,
                new CapturingSourcePassportStore()
        );

        assertThrows(IllegalArgumentException.class, synchronizer::synchronizeStoreFromSourceRegistry);
    }

    private static SourcePassport passport(String displayName) {
        return new SourcePassport(SourceDisplayName.of(displayName), displayName + " description");
    }

    private record TestSource(
            SourceCode code,
            SourcePassport passport
    ) implements MarketDataSource {
        @Override
        public Set<? extends InstrumentHandler<? extends MarketDataSource, ? extends Instrument>> handlers() {
            return Set.of();
        }

        @Override
        public <I extends Instrument> Publisher<SourceTick> streamSourceTicks(I instrument) {
            throw new UnsupportedOperationException("Test source does not stream ticks");
        }
    }

    private static final class CapturingSourcePassportStore implements SourcePassportStore {
        private Set<SourceCode> retiredAllExcept = Set.of();
        private List<SourcePassportRecord> savedRecords = List.of();

        @Override
        public void retireAllExcept(Set<SourceCode> registeredSourceCodes) {
            retiredAllExcept = new LinkedHashSet<>(registeredSourceCodes);
        }

        @Override
        public void save(Collection<SourcePassportRecord> passports) {
            savedRecords = List.copyOf(passports);
        }
    }
}
