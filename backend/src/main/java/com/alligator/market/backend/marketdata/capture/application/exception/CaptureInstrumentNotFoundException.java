package com.alligator.market.backend.marketdata.capture.application.exception;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.Objects;

/**
 * Ошибка application-слоя: инструмент не найден для сценария фиксации рыночных данных.
 */
public final class CaptureInstrumentNotFoundException extends IllegalStateException {

    private final InstrumentCode instrumentCode;

    public CaptureInstrumentNotFoundException(InstrumentCode instrumentCode) {
        super("Instrument not found for market data capture: instrumentCode=%s".formatted(
                Objects.requireNonNull(instrumentCode, "instrumentCode must not be null").value()
        ));
        this.instrumentCode = instrumentCode;
    }

    public InstrumentCode instrumentCode() {
        return instrumentCode;
    }
}
