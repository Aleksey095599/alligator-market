package com.alligator.market.backend.process.quotemonitor.application.instrument.sync;

import com.alligator.market.domain.process.quotemonitor.instrument.registry.sync.RuntimeQuoteMonitorInstrumentSelectionRegistryUpdater;

import java.util.Objects;

public final class QuoteMonitorInstrumentSelectionRegistrySynchronizationService {
    private final RuntimeQuoteMonitorInstrumentSelectionRegistryUpdater runtimeRegistryUpdater;

    public QuoteMonitorInstrumentSelectionRegistrySynchronizationService(
            RuntimeQuoteMonitorInstrumentSelectionRegistryUpdater runtimeRegistryUpdater
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
