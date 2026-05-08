package com.alligator.market.backend.marketdata.capture.process.catalog.twap.fxspot.analytical.lastprice.application.exception;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.source.vo.MarketDataSourceCode;

import java.util.Objects;

/**
 * Application error: the source did not return a source tick for one capture step.
 */
public final class AnalyticalFxSpotTwapLastPriceSourceTickNotReceivedException extends IllegalStateException {

    public AnalyticalFxSpotTwapLastPriceSourceTickNotReceivedException(
            InstrumentCode instrumentCode,
            MarketDataSourceCode sourceCode
    ) {
        super("Source tick was not received for analytical FX_SPOT TWAP last price capture "
                + "(instrumentCode=%s, sourceCode=%s)".formatted(
                Objects.requireNonNull(instrumentCode, "instrumentCode must not be null").value(),
                Objects.requireNonNull(sourceCode, "sourceCode must not be null").value()
        ));
    }
}
