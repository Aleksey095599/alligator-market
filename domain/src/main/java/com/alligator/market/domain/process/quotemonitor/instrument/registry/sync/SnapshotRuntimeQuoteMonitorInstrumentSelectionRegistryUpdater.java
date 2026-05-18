package com.alligator.market.domain.process.quotemonitor.instrument.registry.sync;

import com.alligator.market.domain.process.quotemonitor.instrument.QuoteMonitorInstrumentSelection;
import com.alligator.market.domain.process.quotemonitor.instrument.registry.runtime.RuntimeQuoteMonitorInstrumentSelectionRegistry;
import com.alligator.market.domain.process.quotemonitor.instrument.registry.runtime.SnapshotRuntimeQuoteMonitorInstrumentSelectionRegistry;
import com.alligator.market.domain.process.quotemonitor.instrument.registry.stored.StoredQuoteMonitorInstrumentSelectionRegistry;

import java.util.Objects;

public final class SnapshotRuntimeQuoteMonitorInstrumentSelectionRegistryUpdater
        implements RuntimeQuoteMonitorInstrumentSelectionRegistryUpdater {
    private final StoredQuoteMonitorInstrumentSelectionRegistry storedRegistry;
    private final RuntimeQuoteMonitorInstrumentSelectionRegistryPublisher runtimeRegistryPublisher;

    public SnapshotRuntimeQuoteMonitorInstrumentSelectionRegistryUpdater(
            StoredQuoteMonitorInstrumentSelectionRegistry storedRegistry,
            RuntimeQuoteMonitorInstrumentSelectionRegistryPublisher runtimeRegistryPublisher
    ) {
        this.storedRegistry = Objects.requireNonNull(
                storedRegistry,
                "storedRegistry must not be null"
        );
        this.runtimeRegistryPublisher = Objects.requireNonNull(
                runtimeRegistryPublisher,
                "runtimeRegistryPublisher must not be null"
        );
    }

    @Override
    public void updateRuntimeRegistry() {
        QuoteMonitorInstrumentSelection selection = loadSelection();
        RuntimeQuoteMonitorInstrumentSelectionRegistry snapshot = createSnapshot(selection);
        publishSnapshot(snapshot);
    }

    private QuoteMonitorInstrumentSelection loadSelection() {
        return storedRegistry.getSelection();
    }

    private RuntimeQuoteMonitorInstrumentSelectionRegistry createSnapshot(
            QuoteMonitorInstrumentSelection selection
    ) {
        return new SnapshotRuntimeQuoteMonitorInstrumentSelectionRegistry(selection);
    }

    private void publishSnapshot(RuntimeQuoteMonitorInstrumentSelectionRegistry snapshot) {
        runtimeRegistryPublisher.replaceWith(snapshot);
    }
}
