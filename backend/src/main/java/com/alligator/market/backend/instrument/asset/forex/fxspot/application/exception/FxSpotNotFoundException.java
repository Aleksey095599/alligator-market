package com.alligator.market.backend.instrument.asset.forex.fxspot.application.exception;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.Objects;

/**
 * Ошибка application-слоя: FX_SPOT инструмент не найден.
 */
public final class FxSpotNotFoundException extends IllegalStateException {

    private final InstrumentCode instrumentCode;

    public FxSpotNotFoundException(InstrumentCode instrumentCode) {
        super("FX_SPOT instrument not found (code=%s)".formatted(
                Objects.requireNonNull(instrumentCode, "instrumentCode must not be null").value()
        ));
        this.instrumentCode = instrumentCode;
    }

    public InstrumentCode instrumentCode() {
        return instrumentCode;
    }
}
