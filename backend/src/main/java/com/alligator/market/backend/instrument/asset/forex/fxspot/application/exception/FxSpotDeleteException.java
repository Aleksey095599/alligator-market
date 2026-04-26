package com.alligator.market.backend.instrument.asset.forex.fxspot.application.exception;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.Objects;

/**
 * Ошибка application-слоя: сбой при удалении FX_SPOT инструмента.
 */
public final class FxSpotDeleteException extends IllegalStateException {

    private final InstrumentCode instrumentCode;

    public FxSpotDeleteException(InstrumentCode instrumentCode, Throwable cause) {
        super("Failed to delete FX_SPOT instrument (code=%s)".formatted(
                Objects.requireNonNull(instrumentCode, "instrumentCode must not be null").value()
        ), cause);
        this.instrumentCode = instrumentCode;
    }

    public InstrumentCode instrumentCode() {
        return instrumentCode;
    }
}
