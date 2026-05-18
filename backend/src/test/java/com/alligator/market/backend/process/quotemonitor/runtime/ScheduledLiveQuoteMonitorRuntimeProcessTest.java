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

class ScheduledLiveQuoteMonitorRuntimeProcessTest {
    private static final Instant TICK_TIME = Instant.parse("2026-05-18T08:00:00Z");

    @Test
    void startSchedulesSingleRuntimeTask() {
        FakeLiveQuoteMonitorProcessScheduler scheduler = new FakeLiveQuoteMonitorProcessScheduler();
        ScheduledLiveQuoteMonitorRuntimeProcess process = process(
                new MutableRuntimeQuoteMonitorInstrumentSelectionRegistry(QuoteMonitorInstrumentSelection.empty()),
                new MutableRuntimeSourcePlanRegistry(List.of()),
                scheduler
        );

        assertThat(process.start()).isTrue();
        assertThat(process.start()).isFalse();

        assertThat(process.status()).isEqualTo(LiveQuoteMonitorRuntimeStatus.RUNNING);
        assertThat(scheduler.scheduleCount).isEqualTo(1);
        assertThat(scheduler.interval).isEqualTo(LiveQuoteMonitorCapturer.UPDATE_INTERVAL);
    }

    @Test
    void stopCancelsRuntimeTask() {
        FakeLiveQuoteMonitorProcessScheduler scheduler = new FakeLiveQuoteMonitorProcessScheduler();
        ScheduledLiveQuoteMonitorRuntimeProcess process = process(
                new MutableRuntimeQuoteMonitorInstrumentSelectionRegistry(QuoteMonitorInstrumentSelection.empty()),
                new MutableRuntimeSourcePlanRegistry(List.of()),
                scheduler
        );

        process.start();

        assertThat(process.stop()).isTrue();
        assertThat(process.stop()).isFalse();

        assertThat(process.status()).isEqualTo(LiveQuoteMonitorRuntimeStatus.STOPPED);
        assertThat(scheduler.scheduledTask.cancelled).isTrue();
    }

    @Test
    void tickUsesCurrentSelectionAndExecutableSourcePlans() {
        InstrumentCode firstCode = InstrumentCode.of("FOREX_SPOT_CNYRUB_TOM");
        InstrumentCode secondCode = InstrumentCode.of("FOREX_SPOT_USDRUB_TOM");
        MutableRuntimeQuoteMonitorInstrumentSelectionRegistry selectionRegistry =
                new MutableRuntimeQuoteMonitorInstrumentSelectionRegistry(
                        new QuoteMonitorInstrumentSelection(List.of(firstCode, secondCode))
                );
        MutableRuntimeSourcePlanRegistry sourcePlanRegistry =
                new MutableRuntimeSourcePlanRegistry(List.of(plan(firstCode)));
        FakeLiveQuoteMonitorProcessScheduler scheduler = new FakeLiveQuoteMonitorProcessScheduler();
        ScheduledLiveQuoteMonitorRuntimeProcess process = process(
                selectionRegistry,
                sourcePlanRegistry,
                scheduler
        );

        process.start();
        scheduler.runScheduledTask();

        assertThat(process.snapshot().monitoredInstrumentCodes()).containsExactly(firstCode);
        assertThat(process.snapshot().lastTickAt()).contains(TICK_TIME);

        selectionRegistry.replaceSelection(new QuoteMonitorInstrumentSelection(List.of(secondCode)));
        sourcePlanRegistry.replaceExecutablePlans(List.of(plan(secondCode)));
        scheduler.runScheduledTask();

        assertThat(process.snapshot().monitoredInstrumentCodes()).containsExactly(secondCode);
    }

    private static ScheduledLiveQuoteMonitorRuntimeProcess process(
            RuntimeQuoteMonitorInstrumentSelectionRegistry selectionRegistry,
            RuntimeSourcePlanRegistry sourcePlanRegistry,
            LiveQuoteMonitorProcessScheduler scheduler
    ) {
        return new ScheduledLiveQuoteMonitorRuntimeProcess(
                new LiveQuoteMonitorCapturer(),
                selectionRegistry,
                sourcePlanRegistry,
                scheduler,
                Clock.fixed(TICK_TIME, ZoneOffset.UTC)
        );
    }

    private static SourcePlan plan(InstrumentCode instrumentCode) {
        return new SourcePlan(
                LiveQuoteMonitorCapturer.CAPTURER_CODE,
                instrumentCode,
                List.of(new SourcePlanEntry(SourceCode.of("PRIMARY_SOURCE"), 0))
        );
    }

    private static final class FakeLiveQuoteMonitorProcessScheduler
            implements LiveQuoteMonitorProcessScheduler {
        private int scheduleCount;
        private java.time.Duration interval;
        private Runnable task;
        private FakeLiveQuoteMonitorScheduledTask scheduledTask;

        @Override
        public LiveQuoteMonitorScheduledTask scheduleAtFixedRate(
                java.time.Duration interval,
                Runnable task
        ) {
            scheduleCount++;
            this.interval = interval;
            this.task = task;
            this.scheduledTask = new FakeLiveQuoteMonitorScheduledTask();
            return scheduledTask;
        }

        private void runScheduledTask() {
            task.run();
        }
    }

    private static final class FakeLiveQuoteMonitorScheduledTask
            implements LiveQuoteMonitorScheduledTask {
        private boolean cancelled;

        @Override
        public void cancel() {
            cancelled = true;
        }
    }

    private static final class MutableRuntimeQuoteMonitorInstrumentSelectionRegistry
            implements RuntimeQuoteMonitorInstrumentSelectionRegistry {
        private QuoteMonitorInstrumentSelection selection;

        private MutableRuntimeQuoteMonitorInstrumentSelectionRegistry(
                QuoteMonitorInstrumentSelection selection
        ) {
            replaceSelection(selection);
        }

        private void replaceSelection(QuoteMonitorInstrumentSelection selection) {
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
        private Map<SourcePlanKey, SourcePlan> plansByKey;

        private MutableRuntimeSourcePlanRegistry(List<SourcePlan> executablePlans) {
            replaceExecutablePlans(executablePlans);
        }

        private void replaceExecutablePlans(List<SourcePlan> executablePlans) {
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
