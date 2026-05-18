package com.alligator.market.backend.process.quotemonitor.registry.runtime;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.process.quotemonitor.instrument.QuoteMonitorInstrumentSelection;
import com.alligator.market.domain.process.quotemonitor.instrument.registry.runtime.RuntimeQuoteMonitorInstrumentSelectionRegistry;
import com.alligator.market.domain.process.quotemonitor.instrument.registry.runtime.SnapshotRuntimeQuoteMonitorInstrumentSelectionRegistry;
import com.alligator.market.domain.process.quotemonitor.instrument.registry.sync.RuntimeQuoteMonitorInstrumentSelectionRegistryPublisher;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public final class AtomicRuntimeQuoteMonitorInstrumentSelectionRegistry
        implements RuntimeQuoteMonitorInstrumentSelectionRegistry,
        RuntimeQuoteMonitorInstrumentSelectionRegistryPublisher {
    private final AtomicReference<RuntimeQuoteMonitorInstrumentSelectionRegistry> currentRegistry =
            new AtomicReference<>(
                    new SnapshotRuntimeQuoteMonitorInstrumentSelectionRegistry(
                            QuoteMonitorInstrumentSelection.empty()
                    )
            );

    @Override
    public QuoteMonitorInstrumentSelection currentSelection() {
        return currentRegistry.get().currentSelection();
    }

    @Override
    public List<InstrumentCode> selectedInstrumentCodes() {
        return currentRegistry.get().selectedInstrumentCodes();
    }

    @Override
    public boolean isSelected(InstrumentCode instrumentCode) {
        return currentRegistry.get().isSelected(instrumentCode);
    }

    @Override
    public void replaceWith(RuntimeQuoteMonitorInstrumentSelectionRegistry registry) {
        currentRegistry.set(Objects.requireNonNull(registry, "registry must not be null"));
    }
}
