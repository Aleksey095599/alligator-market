package com.alligator.market.backend.marketdata.capturer.catalog.twap.fxspot.analytical.lastprice.application.exception;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.capturer.vo.MarketDataCapturerCode;

import java.util.Objects;

/**
 * Ошибка application-слоя: план источников для capture процесса не найден.
 */
public final class AnalyticalFxSpotTwapLastPriceSourcePlanNotFoundException extends IllegalStateException {

    public AnalyticalFxSpotTwapLastPriceSourcePlanNotFoundException(
            MarketDataCapturerCode capturerCode,
            InstrumentCode instrumentCode
    ) {
        super("Market data source plan not found for analytical FX_SPOT TWAP last price capture "
                + "(capturerCode=%s, instrumentCode=%s)".formatted(
                Objects.requireNonNull(capturerCode, "capturerCode must not be null").value(),
                Objects.requireNonNull(instrumentCode, "instrumentCode must not be null").value()
        ));
    }
}
