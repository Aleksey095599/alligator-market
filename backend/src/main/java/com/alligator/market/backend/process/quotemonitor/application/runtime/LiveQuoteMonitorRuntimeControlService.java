package com.alligator.market.backend.process.quotemonitor.application.runtime;

import com.alligator.market.domain.process.quotemonitor.runtime.LiveQuoteMonitorRuntimeProcess;
import com.alligator.market.domain.process.quotemonitor.runtime.LiveQuoteMonitorRuntimeSnapshot;

import java.util.Objects;

public final class LiveQuoteMonitorRuntimeControlService {
    private final LiveQuoteMonitorRuntimeProcess runtimeProcess;

    public LiveQuoteMonitorRuntimeControlService(LiveQuoteMonitorRuntimeProcess runtimeProcess) {
        this.runtimeProcess = Objects.requireNonNull(runtimeProcess, "runtimeProcess must not be null");
    }

    public LiveQuoteMonitorRuntimeSnapshot start() {
        runtimeProcess.start();
        return runtimeProcess.snapshot();
    }

    public LiveQuoteMonitorRuntimeSnapshot stop() {
        runtimeProcess.stop();
        return runtimeProcess.snapshot();
    }

    public LiveQuoteMonitorRuntimeSnapshot snapshot() {
        return runtimeProcess.snapshot();
    }
}
