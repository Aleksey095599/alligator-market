package com.alligator.market.domain.process.quotemonitor.runtime.state.instrument;

import com.alligator.market.domain.shared.code.DomainCodeFormat;

public enum QuoteMonitorInstrumentRuntimeStatus {
    STOPPED(false),
    WAITING_FOR_QUOTE(false),
    LIVE(false),
    RUNTIME_INSTRUMENT_NOT_FOUND(true),
    RUNTIME_SOURCE_PLAN_NOT_FOUND(true),
    RUNTIME_SOURCE_NOT_FOUND(true),
    HANDLER_NOT_FOUND(true),
    INSTRUMENT_NOT_SUPPORTED_BY_HANDLER(true),
    STREAM_START_FAILED(true),
    STREAM_FAILED(true),
    UNSUPPORTED_SOURCE_TICK_TYPE(true);

    private static final int MAX_CODE_LENGTH = 40;
    private final boolean issue;

    QuoteMonitorInstrumentRuntimeStatus(boolean issue) {
        this.issue = issue;

        if (name().length() > MAX_CODE_LENGTH) {
            throw new IllegalStateException(
                    "QuoteMonitorInstrumentRuntimeStatus code must not exceed " +
                            MAX_CODE_LENGTH + " characters: " + name()
            );
        }
        DomainCodeFormat.requireValidEnumCode("QuoteMonitorInstrumentRuntimeStatus", name());
    }

    public boolean isIssue() {
        return issue;
    }
}
