package com.alligator.market.backend.instrument.asset.forex.fxspot.application.exception;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.Objects;

/**
 * Ошибка application-слоя: сбой при обновлении FX_SPOT инструмента.
 */
public final class FxSpotUpdateException extends IllegalStateException {

    private final InstrumentCode instrumentCode;

    public FxSpotUpdateException(InstrumentCode instrumentCode, Throwable cause) {
        super("Failed to update FX_SPOT instrument (code=%s)".formatted(
                Objects.requireNonNull(instrumentCode, "instrumentCode must not be null").value()
        ), cause);
        this.instrumentCode = instrumentCode;
    }

    public InstrumentCode instrumentCode() {
        return instrumentCode;
    }
}
