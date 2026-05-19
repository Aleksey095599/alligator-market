package com.alligator.market.backend.process.quotemonitor.runtime;

import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.process.quotemonitor.capturer.LiveQuoteMonitorCapturer;
import com.alligator.market.domain.process.quotemonitor.instrument.QuoteMonitorInstrumentSelection;
import com.alligator.market.domain.process.quotemonitor.instrument.registry.runtime.RuntimeQuoteMonitorInstrumentSelectionRegistry;
import com.alligator.market.domain.process.quotemonitor.runtime.LiveQuoteMonitorRuntimeStatus;
import com.alligator.market.domain.source.vo.SourceCode;
import com.alligator.market.domain.sourceplan.SourcePlan;
import com.alligator.market.domain.sourceplan.SourcePlanEntry;
import com.alligator.market.domain.sourceplan.SourcePlanKey;
import com.alligator.market.domain.sourceplan.registry.runtime.RuntimeSourcePlanRegistry;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultLiveQuoteMonitorRuntimeProcessTest {
    private static final Instant START_TIME = Instant.parse("2026-05-18T08:00:00Z");

    @Test
    void startMovesProcessToRunningOnlyOnce() {
        DefaultLiveQuoteMonitorRuntimeProcess process = process(
                new MutableRuntimeQuoteMonitorInstrumentSelectionRegistry(QuoteMonitorInstrumentSelection.empty()),
                new MutableRuntimeSourcePlanRegistry(List.of())
        );

        assertThat(process.start()).isTrue();
        assertThat(process.start()).isFalse();

        assertThat(process.status()).isEqualTo(LiveQuoteMonitorRuntimeStatus.RUNNING);
        assertThat(process.snapshot().lastTickAt()).contains(START_TIME);
    }

    @Test
    void stopMovesProcessToStoppedOnlyOnce() {
        DefaultLiveQuoteMonitorRuntimeProcess process = process(
                new MutableRuntimeQuoteMonitorInstrumentSelectionRegistry(QuoteMonitorInstrumentSelection.empty()),
                new MutableRuntimeSourcePlanRegistry(List.of())
        );

        process.start();

        assertThat(process.stop()).isTrue();
        assertThat(process.stop()).isFalse();

        assertThat(process.status()).isEqualTo(LiveQuoteMonitorRuntimeStatus.STOPPED);
    }

    @Test
    void startUsesCurrentSelectionAndExecutableSourcePlans() {
        InstrumentCode firstCode = InstrumentCode.of("FOREX_SPOT_CNYRUB_TOM");
        InstrumentCode secondCode = InstrumentCode.of("FOREX_SPOT_USDRUB_TOM");
        MutableRuntimeQuoteMonitorInstrumentSelectionRegistry selectionRegistry =
                new MutableRuntimeQuoteMonitorInstrumentSelectionRegistry(
                        new QuoteMonitorInstrumentSelection(List.of(firstCode, secondCode))
                );
        MutableRuntimeSourcePlanRegistry sourcePlanRegistry =
                new MutableRuntimeSourcePlanRegistry(List.of(plan(firstCode)));
        DefaultLiveQuoteMonitorRuntimeProcess process = process(selectionRegistry, sourcePlanRegistry);

        process.start();

        assertThat(process.snapshot().monitoredInstrumentCodes()).containsExactly(firstCode);
        assertThat(process.snapshot().lastTickAt()).contains(START_TIME);
    }

    private static DefaultLiveQuoteMonitorRuntimeProcess process(
            RuntimeQuoteMonitorInstrumentSelectionRegistry selectionRegistry,
            RuntimeSourcePlanRegistry sourcePlanRegistry
    ) {
        return new DefaultLiveQuoteMonitorRuntimeProcess(
                new LiveQuoteMonitorCapturer(),
                selectionRegistry,
                sourcePlanRegistry,
                Clock.fixed(START_TIME, ZoneOffset.UTC)
        );
    }

    private static SourcePlan plan(InstrumentCode instrumentCode) {
        return new SourcePlan(
                LiveQuoteMonitorCapturer.CAPTURER_CODE,
                instrumentCode,
                List.of(new SourcePlanEntry(SourceCode.of("PRIMARY_SOURCE"), 0))
        );
    }

    private static final class MutableRuntimeQuoteMonitorInstrumentSelectionRegistry
            implements RuntimeQuoteMonitorInstrumentSelectionRegistry {
        private QuoteMonitorInstrumentSelection selection;

        private MutableRuntimeQuoteMonitorInstrumentSelectionRegistry(
                QuoteMonitorInstrumentSelection selection
        ) {
            this.selection = selection;
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

    private static final class MutableRuntimeSourcePlanRegistry implements RuntimeSourcePlanRegistry {
        private final Map<SourcePlanKey, SourcePlan> plansByKey;

        private MutableRuntimeSourcePlanRegistry(List<SourcePlan> executablePlans) {
            Map<SourcePlanKey, SourcePlan> updatedPlansByKey = new LinkedHashMap<>();

            for (SourcePlan plan : executablePlans) {
                updatedPlansByKey.put(plan.key(), plan);
            }

            this.plansByKey = Map.copyOf(updatedPlansByKey);
        }

        @Override
        public Optional<SourcePlan> findExecutableByKey(SourcePlanKey key) {
            return Optional.ofNullable(plansByKey.get(key));
        }

        @Override
        public List<SourcePlan> findExecutableByCapturerCode(CapturerCode capturerCode) {
            return plansByKey.values()
                    .stream()
                    .filter(plan -> plan.capturerCode().equals(capturerCode))
                    .toList();
        }

        @Override
        public Map<SourcePlanKey, SourcePlan> executablePlansByKey() {
            return plansByKey;
        }
    }
}
