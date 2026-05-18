package com.alligator.market.backend.process.quotemonitor.registry.runtime;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.process.quotemonitor.instrument.QuoteMonitorInstrumentSelection;
import com.alligator.market.domain.process.quotemonitor.instrument.registry.stored.StoredQuoteMonitorInstrumentSelectionRegistry;
import com.alligator.market.domain.process.quotemonitor.instrument.registry.sync.RuntimeQuoteMonitorInstrumentSelectionRegistryUpdater;
import com.alligator.market.domain.process.quotemonitor.instrument.registry.sync.SnapshotRuntimeQuoteMonitorInstrumentSelectionRegistryUpdater;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AtomicRuntimeQuoteMonitorInstrumentSelectionRegistrySyncTest {

    @Test
    void runtimeRegistryReturnsChangedSelectionAfterUpdaterRunsAgain() {
        QuoteMonitorInstrumentSelection firstSelection = selection("FOREX_SPOT_CNYRUB_TOM");
        QuoteMonitorInstrumentSelection changedSelection = selection("FOREX_SPOT_USDRUB_TOM");
        MutableStoredQuoteMonitorInstrumentSelectionRegistry storedRegistry =
                new MutableStoredQuoteMonitorInstrumentSelectionRegistry(firstSelection);
        AtomicRuntimeQuoteMonitorInstrumentSelectionRegistry runtimeRegistry =
                new AtomicRuntimeQuoteMonitorInstrumentSelectionRegistry();
        RuntimeQuoteMonitorInstrumentSelectionRegistryUpdater updater =
                new SnapshotRuntimeQuoteMonitorInstrumentSelectionRegistryUpdater(
                        storedRegistry,
                        runtimeRegistry
                );

        updater.updateRuntimeRegistry();

        assertEquals(firstSelection.instrumentCodes(), runtimeRegistry.currentSelection().instrumentCodes());
        assertTrue(runtimeRegistry.isSelected(InstrumentCode.of("FOREX_SPOT_CNYRUB_TOM")));

        storedRegistry.replaceSelection(changedSelection);
        updater.updateRuntimeRegistry();

        assertEquals(changedSelection.instrumentCodes(), runtimeRegistry.currentSelection().instrumentCodes());
        assertFalse(runtimeRegistry.isSelected(InstrumentCode.of("FOREX_SPOT_CNYRUB_TOM")));
        assertTrue(runtimeRegistry.isSelected(InstrumentCode.of("FOREX_SPOT_USDRUB_TOM")));
    }

    @Test
    void runtimeRegistryIsClearedWhenStoredRegistryHasEmptySelection() {
        QuoteMonitorInstrumentSelection initialSelection = selection("FOREX_SPOT_CNYRUB_TOM");
        MutableStoredQuoteMonitorInstrumentSelectionRegistry storedRegistry =
                new MutableStoredQuoteMonitorInstrumentSelectionRegistry(initialSelection);
        AtomicRuntimeQuoteMonitorInstrumentSelectionRegistry runtimeRegistry =
                new AtomicRuntimeQuoteMonitorInstrumentSelectionRegistry();
        RuntimeQuoteMonitorInstrumentSelectionRegistryUpdater updater =
                new SnapshotRuntimeQuoteMonitorInstrumentSelectionRegistryUpdater(
                        storedRegistry,
                        runtimeRegistry
                );

        updater.updateRuntimeRegistry();
        storedRegistry.replaceSelection(QuoteMonitorInstrumentSelection.empty());
        updater.updateRuntimeRegistry();

        assertTrue(runtimeRegistry.selectedInstrumentCodes().isEmpty());
        assertFalse(runtimeRegistry.isSelected(InstrumentCode.of("FOREX_SPOT_CNYRUB_TOM")));
    }

    private static QuoteMonitorInstrumentSelection selection(String instrumentCode) {
        return new QuoteMonitorInstrumentSelection(List.of(InstrumentCode.of(instrumentCode)));
    }

    private static final class MutableStoredQuoteMonitorInstrumentSelectionRegistry
            implements StoredQuoteMonitorInstrumentSelectionRegistry {
        private QuoteMonitorInstrumentSelection selection;

        private MutableStoredQuoteMonitorInstrumentSelectionRegistry(
                QuoteMonitorInstrumentSelection selection
        ) {
            replaceSelection(selection);
        }

        private void replaceSelection(QuoteMonitorInstrumentSelection selection) {
            this.selection = Objects.requireNonNull(selection, "selection must not be null");
        }

        @Override
        public QuoteMonitorInstrumentSelection getSelection() {
            return selection;
        }
    }
}
