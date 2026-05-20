package com.alligator.market.backend.instrument.application.registry.sync;

import com.alligator.market.domain.instrument.registry.sync.RuntimeInstrumentRegistryUpdater;

import java.util.Objects;

public final class InstrumentRegistrySynchronizationService {
    private final RuntimeInstrumentRegistryUpdater runtimeRegistryUpdater;

    public InstrumentRegistrySynchronizationService(
            RuntimeInstrumentRegistryUpdater runtimeRegistryUpdater
    ) {
        this.runtimeRegistryUpdater = Objects.requireNonNull(
                runtimeRegistryUpdater,
                "runtimeRegistryUpdater must not be null"
        );
    }

    public void synchronize() {
        runtimeRegistryUpdater.updateRuntimeRegistry();
    }
}
