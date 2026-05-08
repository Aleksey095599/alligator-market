package com.alligator.market.backend.process.twap.application.exception;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.Objects;

/**
 * Ошибка application-слоя: FX_SPOT инструмент для capture процесса не найден.
 */
public final class FxSpotTwapInstrumentNotFoundException extends IllegalStateException {

    public FxSpotTwapInstrumentNotFoundException(InstrumentCode instrumentCode) {
        super("FX_SPOT instrument not found for analytical FX_SPOT TWAP last price capture (instrumentCode=%s)".formatted(
                Objects.requireNonNull(instrumentCode, "instrumentCode must not be null").value()
        ));
    }
}
