package com.alligator.market.backend.instrument.asset.forex.fxspot.application.exception;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.Objects;

/**
 * Ошибка application-слоя: FX_SPOT инструмент используется в связанных сценариях.
 */
public final class FxSpotInUseException extends IllegalStateException {

    private final InstrumentCode instrumentCode;

    public FxSpotInUseException(InstrumentCode instrumentCode) {
        super("FX_SPOT instrument '%s' is in use".formatted(
                Objects.requireNonNull(instrumentCode, "instrumentCode must not be null").value()));
        this.instrumentCode = instrumentCode;
    }

    public InstrumentCode instrumentCode() {
        return instrumentCode;
    }
}
