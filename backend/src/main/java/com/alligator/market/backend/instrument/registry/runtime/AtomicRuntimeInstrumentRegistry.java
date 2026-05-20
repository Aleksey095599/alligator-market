package com.alligator.market.backend.instrument.registry.runtime;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.registry.runtime.RuntimeInstrumentRegistry;
import com.alligator.market.domain.instrument.registry.runtime.SnapshotRuntimeInstrumentRegistry;
import com.alligator.market.domain.instrument.registry.sync.RuntimeInstrumentRegistryPublisher;
import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public final class AtomicRuntimeInstrumentRegistry
        implements RuntimeInstrumentRegistry, RuntimeInstrumentRegistryPublisher {
    private final AtomicReference<RuntimeInstrumentRegistry> currentRegistry =
            new AtomicReference<>(new SnapshotRuntimeInstrumentRegistry(List.of()));

    @Override
    public Optional<Instrument> findByCode(InstrumentCode instrumentCode) {
        return currentRegistry.get().findByCode(instrumentCode);
    }

    @Override
    public Map<InstrumentCode, Instrument> instrumentsByCode() {
        return currentRegistry.get().instrumentsByCode();
    }

    @Override
    public void replaceWith(RuntimeInstrumentRegistry registry) {
        currentRegistry.set(Objects.requireNonNull(registry, "registry must not be null"));
    }
}
