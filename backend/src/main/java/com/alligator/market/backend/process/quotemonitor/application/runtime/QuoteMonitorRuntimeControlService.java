package com.alligator.market.backend.process.quotemonitor.application.runtime;

import com.alligator.market.domain.process.quotemonitor.runtime.QuoteMonitorRuntimeProcess;
import com.alligator.market.domain.process.quotemonitor.runtime.state.QuoteMonitorRuntimeState;

import java.util.Objects;

public final class QuoteMonitorRuntimeControlService {
    private final QuoteMonitorRuntimeProcess runtimeProcess;

    public QuoteMonitorRuntimeControlService(QuoteMonitorRuntimeProcess runtimeProcess) {
        this.runtimeProcess = Objects.requireNonNull(runtimeProcess, "runtimeProcess must not be null");
    }

    public QuoteMonitorRuntimeState start() {
        runtimeProcess.start();
        return runtimeProcess.state();
    }

    public QuoteMonitorRuntimeState stop() {
        runtimeProcess.stop();
        return runtimeProcess.state();
    }

    public QuoteMonitorRuntimeState state() {
        return runtimeProcess.state();
    }
}
