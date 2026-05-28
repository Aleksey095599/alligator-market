package com.alligator.market.domain.process.quotemonitor.runtime.start;

import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.instrument.Asset;
import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.Product;
import com.alligator.market.domain.instrument.registry.runtime.RuntimeInstrumentRegistry;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.instrument.vo.InstrumentSymbol;
import com.alligator.market.domain.marketdata.tick.level.source.SourceTick;
import com.alligator.market.domain.process.quotemonitor.capturer.QuoteMonitorCapturer;
import com.alligator.market.domain.process.quotemonitor.instrument.QuoteMonitorInstrumentSelection;
import com.alligator.market.domain.process.quotemonitor.instrument.registry.runtime.RuntimeQuoteMonitorInstrumentSelectionRegistry;
import com.alligator.market.domain.process.quotemonitor.runtime.state.QuoteMonitorRuntimeStatus;
import com.alligator.market.domain.process.quotemonitor.runtime.state.instrument.QuoteMonitorInstrumentRuntimeState;
import com.alligator.market.domain.process.quotemonitor.runtime.state.instrument.QuoteMonitorInstrumentRuntimeStatus;
import com.alligator.market.domain.source.MarketSource;
import com.alligator.market.domain.source.passport.SourcePassport;
import com.alligator.market.domain.source.passport.vo.SourceDisplayName;
import com.alligator.market.domain.source.registry.RuntimeSourceRegistry;
import com.alligator.market.domain.source.vo.SourceCode;
import com.alligator.market.domain.sourceplan.vo.PrioritizedSourceCode;
import com.alligator.market.domain.sourceplan.SourcePlan;
import com.alligator.market.domain.sourceplan.vo.SourcePlanKey;
import com.alligator.market.domain.sourceplan.registry.runtime.RuntimeSourcePlanRegistry;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuoteMonitorRuntimeStartTest {

    @Test
    void resolvesSourceAssignmentFromSourcePlanAndInitialState() {
        InstrumentCode instrumentCode = InstrumentCode.of("FOREX_SPOT_CNYRUB_TOM");
        SourceCode missingSourceCode = SourceCode.of("MISSING_SOURCE");
        SourceCode backupSourceCode = SourceCode.of("BACKUP_SOURCE");
        QuoteMonitorRuntimeStart runtimeStart = runtimeStart(
                new RuntimeSelection(new QuoteMonitorInstrumentSelection(List.of(instrumentCode))),
                new RuntimeInstruments(List.of(instrument(instrumentCode))),
                new RuntimeSourcePlans(List.of(new SourcePlan(
                        QuoteMonitorCapturer.CAPTURER_CODE,
                        instrumentCode,
                        List.of(
                                new PrioritizedSourceCode(missingSourceCode, 0),
                                new PrioritizedSourceCode(backupSourceCode, 1)
                        )
                ))),
                new RuntimeSources(List.of(source(backupSourceCode)))
        );

        QuoteMonitorRuntimeStartResolution resolution = runtimeStart.resolve();

        assertEquals(QuoteMonitorRuntimeStatus.RUNNING, resolution.initialState().status());
        assertEquals(List.of(instrumentCode), resolution.monitoredInstrumentCodes());
        assertEquals(1, resolution.sourceAssignments().size());
        assertEquals(backupSourceCode, resolution.sourceAssignments().getFirst().prioritizedSourceCode().sourceCode());
        assertEquals(backupSourceCode, resolution.sourceAssignments().getFirst().sourceCode());

        QuoteMonitorInstrumentRuntimeState instrumentState =
                resolution.initialState().instrumentState(instrumentCode).orElseThrow();
        assertEquals(QuoteMonitorInstrumentRuntimeStatus.WAITING_FOR_QUOTE, instrumentState.status());
        assertEquals(backupSourceCode, instrumentState.sourceCode().orElseThrow());
    }

    @Test
    void recordsRuntimeIssuesBeforeBackendStartsStreams() {
        InstrumentCode missingInstrumentCode = InstrumentCode.of("FOREX_SPOT_CNYRUB_TOM");
        InstrumentCode missingPlanCode = InstrumentCode.of("FOREX_SPOT_USDRUB_TOM");
        InstrumentCode missingSourceCode = InstrumentCode.of("FOREX_SPOT_EURRUB_TOM");
        QuoteMonitorRuntimeStart runtimeStart = runtimeStart(
                new RuntimeSelection(new QuoteMonitorInstrumentSelection(List.of(
                        missingInstrumentCode,
                        missingPlanCode,
                        missingSourceCode
                ))),
                new RuntimeInstruments(List.of(
                        instrument(missingPlanCode),
                        instrument(missingSourceCode)
                )),
                new RuntimeSourcePlans(List.of(plan(missingSourceCode))),
                new RuntimeSources(List.of())
        );

        QuoteMonitorRuntimeStartResolution resolution = runtimeStart.resolve();

        assertTrue(resolution.sourceAssignments().isEmpty());
        assertTrue(resolution.initialState().hasInstrumentIssues());
        assertEquals(
                QuoteMonitorInstrumentRuntimeStatus.RUNTIME_INSTRUMENT_NOT_FOUND,
                resolution.initialState().instrumentState(missingInstrumentCode).orElseThrow().status()
        );
        assertEquals(
                QuoteMonitorInstrumentRuntimeStatus.RUNTIME_SOURCE_PLAN_NOT_FOUND,
                resolution.initialState().instrumentState(missingPlanCode).orElseThrow().status()
        );
        assertEquals(
                QuoteMonitorInstrumentRuntimeStatus.RUNTIME_SOURCE_NOT_FOUND,
                resolution.initialState().instrumentState(missingSourceCode).orElseThrow().status()
        );
    }

    private static QuoteMonitorRuntimeStart runtimeStart(
            RuntimeQuoteMonitorInstrumentSelectionRegistry selectionRegistry,
            RuntimeInstrumentRegistry instrumentRegistry,
            RuntimeSourcePlanRegistry sourcePlanRegistry,
            RuntimeSourceRegistry sourceRegistry
    ) {
        return new RegistryBackedQuoteMonitorRuntimeStart(
                new QuoteMonitorCapturer(),
                selectionRegistry,
                instrumentRegistry,
                sourcePlanRegistry,
                sourceRegistry
        );
    }

    private static SourcePlan plan(InstrumentCode instrumentCode) {
        return new SourcePlan(
                QuoteMonitorCapturer.CAPTURER_CODE,
                instrumentCode,
                List.of(new PrioritizedSourceCode(SourceCode.of("PRIMARY_SOURCE"), 0))
        );
    }

    private static TestInstrument instrument(InstrumentCode instrumentCode) {
        return new TestInstrument(instrumentCode);
    }

    private static TestSource source(SourceCode sourceCode) {
        return new TestSource(sourceCode);
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

    private record TestSource(SourceCode code) implements MarketSource {

        @Override
        public SourcePassport passport() {
            return new SourcePassport(SourceDisplayName.of("Test Source"));
        }

        @Override
        public <I extends Instrument> Publisher<SourceTick> streamSourceTicks(I instrument) {
            return subscriber -> {
            };
        }
    }

    private record RuntimeSelection(
            QuoteMonitorInstrumentSelection selection
    ) implements RuntimeQuoteMonitorInstrumentSelectionRegistry {

        private RuntimeSelection {
            selection = new QuoteMonitorInstrumentSelection(selection.instrumentCodes());
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
            return selection.contains(instrumentCode);
        }
    }

    private record RuntimeInstruments(
            Map<InstrumentCode, Instrument> instrumentsByCode
    ) implements RuntimeInstrumentRegistry {

        private RuntimeInstruments(List<? extends Instrument> instruments) {
            this(instrumentsByCode(instruments));
        }

        @Override
        public Optional<Instrument> findByCode(InstrumentCode instrumentCode) {
            return Optional.ofNullable(instrumentsByCode.get(instrumentCode));
        }

        private static Map<InstrumentCode, Instrument> instrumentsByCode(List<? extends Instrument> instruments) {
            Map<InstrumentCode, Instrument> updatedInstrumentsByCode = new LinkedHashMap<>();

            for (Instrument instrument : instruments) {
                updatedInstrumentsByCode.put(instrument.instrumentCode(), instrument);
            }

            return Map.copyOf(updatedInstrumentsByCode);
        }
    }

    private record RuntimeSourcePlans(
            Map<SourcePlanKey, SourcePlan> availablePlansByKey
    ) implements RuntimeSourcePlanRegistry {

        private RuntimeSourcePlans(List<SourcePlan> availablePlans) {
            this(availablePlansByKey(availablePlans));
        }

        @Override
        public Optional<SourcePlan> findAvailableByKey(SourcePlanKey key) {
            return Optional.ofNullable(availablePlansByKey.get(key));
        }

        @Override
        public List<SourcePlan> findAvailableByCapturerCode(CapturerCode capturerCode) {
            return availablePlansByKey.values()
                    .stream()
                    .filter(plan -> plan.capturerCode().equals(capturerCode))
                    .toList();
        }

        @Override
        public Map<SourcePlanKey, SourcePlan> availablePlansByKey() {
            return availablePlansByKey;
        }

        private static Map<SourcePlanKey, SourcePlan> availablePlansByKey(List<SourcePlan> availablePlans) {
            Map<SourcePlanKey, SourcePlan> updatedPlansByKey = new LinkedHashMap<>();

            for (SourcePlan plan : availablePlans) {
                updatedPlansByKey.put(plan.key(), plan);
            }

            return Map.copyOf(updatedPlansByKey);
        }
    }

    private record RuntimeSources(
            Map<SourceCode, MarketSource> sourcesByCode
    ) implements RuntimeSourceRegistry {

        private RuntimeSources(List<? extends MarketSource> sources) {
            this(sourcesByCode(sources));
        }

        private static Map<SourceCode, MarketSource> sourcesByCode(List<? extends MarketSource> sources) {
            Map<SourceCode, MarketSource> updatedSourcesByCode = new LinkedHashMap<>();

            for (MarketSource source : sources) {
                updatedSourcesByCode.put(source.code(), source);
            }

            return Map.copyOf(updatedSourcesByCode);
        }
    }
}
