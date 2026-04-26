package com.alligator.market.backend.instrument.asset.forex.fxspot.application.exception;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.Objects;

/**
 * Ошибка application-слоя: FX_SPOT инструмент с таким кодом уже существует.
 */
public final class FxSpotAlreadyExistsException extends IllegalStateException {

    private final InstrumentCode instrumentCode;

    public FxSpotAlreadyExistsException(InstrumentCode instrumentCode) {
        super("FX_SPOT instrument already exists (code=%s)".formatted(
                Objects.requireNonNull(instrumentCode, "instrumentCode must not be null").value()
        ));
        this.instrumentCode = instrumentCode;
    }

    public InstrumentCode instrumentCode() {
        return instrumentCode;
    }
}
