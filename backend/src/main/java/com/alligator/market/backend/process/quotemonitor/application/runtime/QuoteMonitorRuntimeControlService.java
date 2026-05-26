package com.alligator.market.backend.process.quotemonitor.application.runtime;

import com.alligator.market.domain.process.quotemonitor.runtime.QuoteMonitorRuntimeProcess;
import com.alligator.market.domain.process.quotemonitor.runtime.QuoteMonitorRuntimeSnapshot;

import java.util.Objects;

public final class QuoteMonitorRuntimeControlService {
    private final QuoteMonitorRuntimeProcess runtimeProcess;

    public QuoteMonitorRuntimeControlService(QuoteMonitorRuntimeProcess runtimeProcess) {
        this.runtimeProcess = Objects.requireNonNull(runtimeProcess, "runtimeProcess must not be null");
    }

    public QuoteMonitorRuntimeSnapshot start() {
        runtimeProcess.start();
        return runtimeProcess.snapshot();
    }

    public QuoteMonitorRuntimeSnapshot stop() {
        runtimeProcess.stop();
        return runtimeProcess.snapshot();
    }

    public QuoteMonitorRuntimeSnapshot snapshot() {
        return runtimeProcess.snapshot();
    }
}
