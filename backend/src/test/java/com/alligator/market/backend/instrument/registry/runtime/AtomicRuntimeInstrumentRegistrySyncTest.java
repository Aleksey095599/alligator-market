package com.alligator.market.backend.instrument.registry.runtime;

import com.alligator.market.domain.instrument.Asset;
import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.Product;
import com.alligator.market.domain.instrument.registry.sync.RuntimeInstrumentRegistryUpdater;
import com.alligator.market.domain.instrument.registry.sync.SnapshotRuntimeInstrumentRegistryUpdater;
import com.alligator.market.domain.instrument.repository.InstrumentRepository;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.instrument.vo.InstrumentSymbol;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AtomicRuntimeInstrumentRegistrySyncTest {

    @Test
    void runtimeRegistryReturnsChangedInstrumentAfterUpdaterRunsAgain() {
        TestInstrument firstInstrument = instrument("FOREX_SPOT_CNYRUB_TOM");
        TestInstrument changedInstrument = instrument("FOREX_SPOT_USDRUB_TOM");
        MutableInstrumentRepository repository = new MutableInstrumentRepository(List.of(firstInstrument));
        AtomicRuntimeInstrumentRegistry runtimeRegistry = new AtomicRuntimeInstrumentRegistry();
        RuntimeInstrumentRegistryUpdater updater = new SnapshotRuntimeInstrumentRegistryUpdater(
                List.of(repository),
                runtimeRegistry
        );

        updater.updateRuntimeRegistry();

        assertEquals(Optional.of(firstInstrument), runtimeRegistry.findByCode(firstInstrument.instrumentCode()));

        repository.replaceInstruments(List.of(changedInstrument));
        updater.updateRuntimeRegistry();

        assertEquals(Optional.empty(), runtimeRegistry.findByCode(firstInstrument.instrumentCode()));
        assertEquals(Optional.of(changedInstrument), runtimeRegistry.findByCode(changedInstrument.instrumentCode()));
    }

    @Test
    void runtimeRegistryIsClearedWhenRepositoriesHaveNoInstruments() {
        TestInstrument instrument = instrument("FOREX_SPOT_CNYRUB_TOM");
        MutableInstrumentRepository repository = new MutableInstrumentRepository(List.of(instrument));
        AtomicRuntimeInstrumentRegistry runtimeRegistry = new AtomicRuntimeInstrumentRegistry();
        RuntimeInstrumentRegistryUpdater updater = new SnapshotRuntimeInstrumentRegistryUpdater(
                List.of(repository),
                runtimeRegistry
        );

        updater.updateRuntimeRegistry();
        repository.replaceInstruments(List.of());
        updater.updateRuntimeRegistry();

        assertTrue(runtimeRegistry.instrumentsByCode().isEmpty());
        assertEquals(Optional.empty(), runtimeRegistry.findByCode(instrument.instrumentCode()));
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

    private static final class MutableInstrumentRepository implements InstrumentRepository<TestInstrument> {
        private List<TestInstrument> instruments;

        private MutableInstrumentRepository(List<TestInstrument> instruments) {
            replaceInstruments(instruments);
        }

        private void replaceInstruments(List<TestInstrument> instruments) {
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
}
