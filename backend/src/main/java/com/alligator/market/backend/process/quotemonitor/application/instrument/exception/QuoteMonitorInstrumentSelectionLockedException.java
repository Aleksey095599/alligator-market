package com.alligator.market.backend.process.quotemonitor.application.instrument.exception;

public final class QuoteMonitorInstrumentSelectionLockedException extends IllegalStateException {
    public QuoteMonitorInstrumentSelectionLockedException() {
        super("Quote monitor instrument selection cannot be changed while runtime is running");
    }
}
