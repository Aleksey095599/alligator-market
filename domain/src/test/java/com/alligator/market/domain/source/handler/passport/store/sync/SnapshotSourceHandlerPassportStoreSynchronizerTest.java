package com.alligator.market.domain.source.handler.passport.store.sync;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.tick.level.source.SourceTick;
import com.alligator.market.domain.source.MarketDataSource;
import com.alligator.market.domain.source.handler.InstrumentHandler;
import com.alligator.market.domain.source.handler.passport.AccessMethod;
import com.alligator.market.domain.source.handler.passport.DeliveryMode;
import com.alligator.market.domain.source.handler.passport.SourceHandlerPassport;
import com.alligator.market.domain.source.handler.passport.store.SourceHandlerPassportKey;
import com.alligator.market.domain.source.handler.passport.store.SourceHandlerPassportRecord;
import com.alligator.market.domain.source.handler.passport.store.SourceHandlerPassportStore;
import com.alligator.market.domain.source.handler.policy.SourceHandlerPolicy;
import com.alligator.market.domain.source.passport.SourcePassport;
import com.alligator.market.domain.source.passport.vo.SourceDisplayName;
import com.alligator.market.domain.source.registry.RuntimeSourceRegistry;
import com.alligator.market.domain.source.registry.SnapshotRuntimeSourceRegistry;
import com.alligator.market.domain.source.vo.HandlerCode;
import com.alligator.market.domain.source.vo.SourceCode;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SnapshotSourceHandlerPassportStoreSynchronizerTest {

    @Test
    void synchronizesStoreFromSourceRegistrySnapshot() {
        SourceCode sourceCode = SourceCode.of("TEST_SOURCE");
        HandlerCode handlerCode = HandlerCode.of("TEST_HANDLER");
        SourceHandlerPassport passport = new TestSourceHandlerPassport(
                DeliveryMode.PULL,
                AccessMethod.API_POLL
        );
        RuntimeSourceRegistry sourceRegistry = new SnapshotRuntimeSourceRegistry(List.of(
                new TestSource(sourceCode, Set.of(new TestHandler(handlerCode, passport)))
        ));
        CapturingSourceHandlerPassportStore passportStore = new CapturingSourceHandlerPassportStore();
        SourceHandlerPassportStoreSynchronizer synchronizer = new SnapshotSourceHandlerPassportStoreSynchronizer(
                sourceRegistry,
                passportStore
        );

        synchronizer.synchronizeStoreFromSourceRegistry();

        SourceHandlerPassportKey key = new SourceHandlerPassportKey(sourceCode, handlerCode);
        assertEquals(Set.of(key), passportStore.retiredAllExcept);
        assertEquals(
                List.of(SourceHandlerPassportRecord.registered(sourceCode, handlerCode, passport)),
                passportStore.savedRecords
        );
    }

    @Test
    void rejectsDuplicateHandlerCodesWithinSource() {
        SourceHandlerPassport passport = new TestSourceHandlerPassport(
                DeliveryMode.PULL,
                AccessMethod.API_POLL
        );
        RuntimeSourceRegistry sourceRegistry = new SnapshotRuntimeSourceRegistry(List.of(
                new TestSource(
                        SourceCode.of("TEST_SOURCE"),
                        Set.of(
                                new TestHandler(HandlerCode.of("DUPLICATE_HANDLER"), passport),
                                new TestHandler(
                                        HandlerCode.of("duplicate_handler"),
                                        new TestSourceHandlerPassport(
                                                DeliveryMode.PUSH,
                                                AccessMethod.WEBSOCKET
                                        )
                                )
                        )
                )
        ));
        SourceHandlerPassportStoreSynchronizer synchronizer = new SnapshotSourceHandlerPassportStoreSynchronizer(
                sourceRegistry,
                new CapturingSourceHandlerPassportStore()
        );

        assertThrows(IllegalArgumentException.class, synchronizer::synchronizeStoreFromSourceRegistry);
    }

    private record TestSource(
            SourceCode code,
            Set<TestHandler> handlers
    ) implements MarketDataSource {
        @Override
        public SourcePassport passport() {
            return new SourcePassport(
                    SourceDisplayName.of(code.value()),
                    code.value() + " description"
            );
        }

        @Override
        public <I extends Instrument> Publisher<SourceTick> streamSourceTicks(I instrument) {
            throw new UnsupportedOperationException("Test source does not stream ticks");
        }
    }

    private record TestHandler(
            HandlerCode handlerCode,
            SourceHandlerPassport passport
    ) implements InstrumentHandler<TestSource, Instrument> {
        @Override
        public Set<InstrumentCode> supportedInstrumentCodes() {
            return Set.of();
        }

        @Override
        public SourceHandlerPolicy policy() {
            return new SourceHandlerPolicy() {
            };
        }

        @Override
        public void attachTo(TestSource source) {
        }

        @Override
        public Publisher<SourceTick> streamSourceTicks(Instrument instrument) {
            throw new UnsupportedOperationException("Test handler does not stream ticks");
        }
    }

    private record TestSourceHandlerPassport(
            DeliveryMode deliveryMode,
            AccessMethod accessMethod
    ) implements SourceHandlerPassport {
    }

    private static final class CapturingSourceHandlerPassportStore implements SourceHandlerPassportStore {
        private Set<SourceHandlerPassportKey> retiredAllExcept = Set.of();
        private List<SourceHandlerPassportRecord> savedRecords = List.of();

        @Override
        public void retireAllExcept(Set<SourceHandlerPassportKey> registeredSourceHandlerPassportKeys) {
            retiredAllExcept = new LinkedHashSet<>(registeredSourceHandlerPassportKeys);
        }

        @Override
        public void save(Collection<SourceHandlerPassportRecord> passports) {
            savedRecords = List.copyOf(passports);
        }
    }
}
