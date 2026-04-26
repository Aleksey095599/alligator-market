package com.alligator.market.backend.instrument.asset.forex.fxspot.application.exception;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.Objects;

/**
 * Ошибка application-слоя: сбой при создании FX_SPOT инструмента.
 */
public final class FxSpotCreateException extends IllegalStateException {

    private final InstrumentCode instrumentCode;

    public FxSpotCreateException(InstrumentCode instrumentCode, Throwable cause) {
        super("Failed to create FX_SPOT instrument (code=%s)".formatted(
                Objects.requireNonNull(instrumentCode, "instrumentCode must not be null").value()
        ), cause);
        this.instrumentCode = instrumentCode;
    }

    public InstrumentCode instrumentCode() {
        return instrumentCode;
    }
}
