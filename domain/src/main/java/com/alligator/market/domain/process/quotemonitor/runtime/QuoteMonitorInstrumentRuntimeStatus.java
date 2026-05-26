package com.alligator.market.domain.process.quotemonitor.runtime;

import com.alligator.market.domain.shared.code.DomainCodeFormat;

public enum QuoteMonitorInstrumentRuntimeStatus {
    STOPPED,
    WAITING_FOR_QUOTE,
    LIVE,
    RUNTIME_INSTRUMENT_NOT_FOUND,
    RUNTIME_SOURCE_PLAN_NOT_FOUND,
    RUNTIME_SOURCE_NOT_FOUND,
    HANDLER_NOT_FOUND,
    INSTRUMENT_NOT_SUPPORTED_BY_HANDLER,
    STREAM_START_FAILED,
    STREAM_FAILED,
    UNSUPPORTED_SOURCE_TICK_TYPE;

    private static final int MAX_CODE_LENGTH = 40;

    QuoteMonitorInstrumentRuntimeStatus() {
        if (name().length() > MAX_CODE_LENGTH) {
            throw new IllegalStateException(
                    "QuoteMonitorInstrumentRuntimeStatus code must not exceed " +
                            MAX_CODE_LENGTH + " characters: " + name()
            );
        }
        DomainCodeFormat.requireValidEnumCode("QuoteMonitorInstrumentRuntimeStatus", name());
    }
}
