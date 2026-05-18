package com.alligator.market.domain.process.quotemonitor.instrument.registry.runtime;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.process.quotemonitor.instrument.QuoteMonitorInstrumentSelection;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class SnapshotRuntimeQuoteMonitorInstrumentSelectionRegistry
        implements RuntimeQuoteMonitorInstrumentSelectionRegistry {
    private final QuoteMonitorInstrumentSelection selection;
    private final Set<InstrumentCode> selectedInstrumentCodes;

    public SnapshotRuntimeQuoteMonitorInstrumentSelectionRegistry(QuoteMonitorInstrumentSelection selection) {
        this.selection = Objects.requireNonNull(selection, "selection must not be null");
        this.selectedInstrumentCodes = Set.copyOf(selection.instrumentCodes());
    }

    @Override
    public QuoteMonitorInstrumentSelection currentSelection() {
        return selection;
    }

    @Override
    public List<InstrumentCode> selectedInstrumentCodes() {
        return selection.instrumentCodes();
    }

    @Override
    public boolean isSelected(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        return selectedInstrumentCodes.contains(instrumentCode);
    }
}
