package com.alligator.market.domain.instrument.registry.sync;

import com.alligator.market.domain.instrument.Asset;
import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.Product;
import com.alligator.market.domain.instrument.registry.runtime.RuntimeInstrumentRegistry;
import com.alligator.market.domain.instrument.repository.InstrumentRepository;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.instrument.vo.InstrumentSymbol;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SnapshotRuntimeInstrumentRegistryUpdaterTest {

    @Test
    void publishesInstrumentsFromRepositoriesAsRuntimeSnapshot() {
        TestInstrument firstInstrument = instrument("FOREX_SPOT_CNYRUB_TOM");
        TestInstrument secondInstrument = instrument("FOREX_SPOT_USDRUB_TOM");
        CapturingRuntimeInstrumentRegistryPublisher publisher =
                new CapturingRuntimeInstrumentRegistryPublisher();
        RuntimeInstrumentRegistryUpdater updater = new SnapshotRuntimeInstrumentRegistryUpdater(
                List.of(
                        new StubInstrumentRepository(List.of(firstInstrument)),
                        new StubInstrumentRepository(List.of(secondInstrument))
                ),
                publisher
        );

        updater.updateRuntimeRegistry();

        assertNotNull(publisher.publishedRegistry);
        assertEquals(
                Optional.of(firstInstrument),
                publisher.publishedRegistry.findByCode(firstInstrument.instrumentCode())
        );
        assertEquals(
                Optional.of(secondInstrument),
                publisher.publishedRegistry.findByCode(secondInstrument.instrumentCode())
        );
    }

    @Test
    void publishesEmptySnapshotWhenRepositoriesHaveNoInstruments() {
        CapturingRuntimeInstrumentRegistryPublisher publisher =
                new CapturingRuntimeInstrumentRegistryPublisher();
        RuntimeInstrumentRegistryUpdater updater = new SnapshotRuntimeInstrumentRegistryUpdater(
                List.of(new StubInstrumentRepository(List.of())),
                publisher
        );

        updater.updateRuntimeRegistry();

        assertNotNull(publisher.publishedRegistry);
        assertTrue(publisher.publishedRegistry.instrumentsByCode().isEmpty());
    }

    private static TestInstrument instrument(String code) {
        return new TestInstrument(InstrumentCode.of(code));
    }

    private record TestInstrument(InstrumentCode instrumentCode) implements Instrument {

        @Override
        public InstrumentSymbol instrumentSymbol() {
            return InstrumentSymbol.of(instrumentCode.value());
        }

        @Override
        public Asset asset() {
            return Asset.FOREX;
        }

        @Override
        public Product product() {
            return Product.SPOT;
        }
    }

    private static final class StubInstrumentRepository implements InstrumentRepository<TestInstrument> {
        private final List<TestInstrument> instruments;

        private StubInstrumentRepository(List<TestInstrument> instruments) {
            this.instruments = List.copyOf(
                    Objects.requireNonNull(instruments, "instruments must not be null")
            );
        }

        @Override
        public Optional<TestInstrument> findByCode(InstrumentCode instrumentCode) {
            return instruments.stream()
                    .filter(instrument -> instrument.instrumentCode().equals(instrumentCode))
                    .findFirst();
        }

        @Override
        public List<TestInstrument> findAll() {
            return instruments;
        }
    }

    private static final class CapturingRuntimeInstrumentRegistryPublisher
            implements RuntimeInstrumentRegistryPublisher {
        private RuntimeInstrumentRegistry publishedRegistry;

        @Override
        public void replaceWith(RuntimeInstrumentRegistry registry) {
            publishedRegistry = Objects.requireNonNull(registry, "registry must not be null");
        }
    }
}
