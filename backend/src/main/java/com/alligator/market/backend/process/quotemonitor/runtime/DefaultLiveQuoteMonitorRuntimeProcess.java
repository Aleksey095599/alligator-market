package com.alligator.market.backend.process.quotemonitor.runtime;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.process.quotemonitor.capturer.LiveQuoteMonitorCapturer;
import com.alligator.market.domain.process.quotemonitor.instrument.registry.runtime.RuntimeQuoteMonitorInstrumentSelectionRegistry;
import com.alligator.market.domain.process.quotemonitor.runtime.LiveQuoteMonitorRuntimeProcess;
import com.alligator.market.domain.process.quotemonitor.runtime.LiveQuoteMonitorRuntimeSnapshot;
import com.alligator.market.domain.process.quotemonitor.runtime.LiveQuoteMonitorRuntimeStatus;
import com.alligator.market.domain.sourceplan.SourcePlanKey;
import com.alligator.market.domain.sourceplan.registry.runtime.RuntimeSourcePlanRegistry;

import java.time.Clock;
import java.util.List;
import java.util.Objects;

public final class DefaultLiveQuoteMonitorRuntimeProcess implements LiveQuoteMonitorRuntimeProcess {
    private final LiveQuoteMonitorCapturer capturer;
    private final RuntimeQuoteMonitorInstrumentSelectionRegistry instrumentSelectionRegistry;
    private final RuntimeSourcePlanRegistry sourcePlanRegistry;
    private final Clock clock;
    private final Object lock = new Object();

    private LiveQuoteMonitorRuntimeSnapshot snapshot = LiveQuoteMonitorRuntimeSnapshot.stopped();

    public DefaultLiveQuoteMonitorRuntimeProcess(
            LiveQuoteMonitorCapturer capturer,
            RuntimeQuoteMonitorInstrumentSelectionRegistry instrumentSelectionRegistry,
            RuntimeSourcePlanRegistry sourcePlanRegistry,
            Clock clock
    ) {
        this.capturer = Objects.requireNonNull(capturer, "capturer must not be null");
        this.instrumentSelectionRegistry = Objects.requireNonNull(
                instrumentSelectionRegistry,
                "instrumentSelectionRegistry must not be null"
        );
        this.sourcePlanRegistry = Objects.requireNonNull(
                sourcePlanRegistry,
                "sourcePlanRegistry must not be null"
        );
        this.clock = Objects.requireNonNull(clock, "clock must not be null");
    }

    @Override
    public boolean start() {
        synchronized (lock) {
            if (snapshot.status() == LiveQuoteMonitorRuntimeStatus.RUNNING) {
                return false;
            }

            snapshot = runningSnapshot();
            return true;
        }
    }

    @Override
    public boolean stop() {
        synchronized (lock) {
            if (snapshot.status() == LiveQuoteMonitorRuntimeStatus.STOPPED) {
                return false;
            }

            snapshot = snapshot.withStatus(LiveQuoteMonitorRuntimeStatus.STOPPED);
            return true;
        }
    }

    @Override
    public LiveQuoteMonitorRuntimeStatus status() {
        synchronized (lock) {
            return snapshot.status();
        }
    }

    @Override
    public LiveQuoteMonitorRuntimeSnapshot snapshot() {
        synchronized (lock) {
            return snapshot;
        }
    }

    private LiveQuoteMonitorRuntimeSnapshot runningSnapshot() {
        return new LiveQuoteMonitorRuntimeSnapshot(
                LiveQuoteMonitorRuntimeStatus.RUNNING,
                resolveExecutableMonitoredInstrumentCodes(),
                clock.instant()
        );
    }

    private List<InstrumentCode> resolveExecutableMonitoredInstrumentCodes() {
        return instrumentSelectionRegistry.selectedInstrumentCodes()
                .stream()
                .filter(this::hasExecutableSourcePlan)
                .toList();
    }

    private boolean hasExecutableSourcePlan(InstrumentCode instrumentCode) {
        SourcePlanKey key = new SourcePlanKey(
                capturer.capturerCode(),
                instrumentCode
        );

        return sourcePlanRegistry.findExecutableByKey(key).isPresent();
    }
}
