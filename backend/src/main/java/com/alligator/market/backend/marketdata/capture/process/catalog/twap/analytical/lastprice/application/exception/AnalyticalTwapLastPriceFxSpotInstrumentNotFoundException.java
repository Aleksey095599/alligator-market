package com.alligator.market.backend.marketdata.capture.process.catalog.twap.analytical.lastprice.application.exception;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.Objects;

/**
 * Ошибка application-слоя: FX_SPOT инструмент для capture процесса не найден.
 */
public final class AnalyticalTwapLastPriceFxSpotInstrumentNotFoundException extends IllegalStateException {

    public AnalyticalTwapLastPriceFxSpotInstrumentNotFoundException(InstrumentCode instrumentCode) {
        super("FX_SPOT instrument not found for analytical TWAP last price capture (instrumentCode=%s)".formatted(
                Objects.requireNonNull(instrumentCode, "instrumentCode must not be null").value()
        ));
    }
}
