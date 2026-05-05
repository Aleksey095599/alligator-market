package com.alligator.market.backend.marketdata.capture.process.catalog.twap.fxspot.analytical.lastprice.application.exception;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.capture.process.vo.MDCaptureProcessCode;

import java.util.Objects;

/**
 * Ошибка application-слоя: план источников для capture процесса не найден.
 */
public final class AnalyticalFxSpotTwapLastPriceSourcePlanNotFoundException extends IllegalStateException {

    public AnalyticalFxSpotTwapLastPriceSourcePlanNotFoundException(
            MDCaptureProcessCode captureProcessCode,
            InstrumentCode instrumentCode
    ) {
        super("Market data source plan not found for analytical FX_SPOT TWAP last price capture "
                + "(captureProcessCode=%s, instrumentCode=%s)".formatted(
                Objects.requireNonNull(captureProcessCode, "captureProcessCode must not be null").value(),
                Objects.requireNonNull(instrumentCode, "instrumentCode must not be null").value()
        ));
    }
}
