package com.alligator.market.backend.process.quotemonitor.application.instrument.exception;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

public final class DuplicateQuoteMonitorInstrumentCodeException extends IllegalArgumentException {
    public DuplicateQuoteMonitorInstrumentCodeException(InstrumentCode instrumentCode) {
        super("Duplicate quote monitor instrument code: " + instrumentCode.value());
    }
}
