package com.alligator.market.backend.process.quotemonitor.application.instrument.model;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.Objects;

public record QuoteMonitorInstrumentOption(
        InstrumentCode instrumentCode,
        boolean selected
) {
    public QuoteMonitorInstrumentOption {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
    }
}
