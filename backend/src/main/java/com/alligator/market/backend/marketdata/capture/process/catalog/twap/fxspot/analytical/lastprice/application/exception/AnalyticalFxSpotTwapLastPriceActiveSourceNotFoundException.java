package com.alligator.market.backend.marketdata.capture.process.catalog.twap.fxspot.analytical.lastprice.application.exception;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.capture.process.vo.CaptureProcessCode;

import java.util.Objects;

/**
 * Ошибка application-слоя: в плане источников нет активного источника.
 */
public final class AnalyticalFxSpotTwapLastPriceActiveSourceNotFoundException extends IllegalStateException {

    public AnalyticalFxSpotTwapLastPriceActiveSourceNotFoundException(
            CaptureProcessCode captureProcessCode,
            InstrumentCode instrumentCode
    ) {
        super("Active market data source not found for analytical FX_SPOT TWAP last price capture "
                + "(captureProcessCode=%s, instrumentCode=%s)".formatted(
                Objects.requireNonNull(captureProcessCode, "captureProcessCode must not be null").value(),
                Objects.requireNonNull(instrumentCode, "instrumentCode must not be null").value()
        ));
    }
}
