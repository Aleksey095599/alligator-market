package com.alligator.market.backend.marketdata.capture.process.catalog.twap.fxspot.analytical.lastprice.application.exception;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.capture.process.vo.MarketDataCaptureProcessCode;
import com.alligator.market.domain.source.vo.MarketDataSourceCode;

import java.util.Objects;

/**
 * Application-layer error: the selected market data source is not available.
 */
public final class AnalyticalFxSpotTwapLastPriceSourceNotFoundException extends IllegalStateException {

    public AnalyticalFxSpotTwapLastPriceSourceNotFoundException(
            MarketDataCaptureProcessCode captureProcessCode,
            InstrumentCode instrumentCode
    ) {
        super("Market data source not found for analytical FX_SPOT TWAP last price capture "
                + "(captureProcessCode=%s, instrumentCode=%s)".formatted(
                Objects.requireNonNull(captureProcessCode, "captureProcessCode must not be null").value(),
                Objects.requireNonNull(instrumentCode, "instrumentCode must not be null").value()
        ));
    }

    public AnalyticalFxSpotTwapLastPriceSourceNotFoundException(MarketDataSourceCode sourceCode) {
        super("Market data source not found for analytical FX_SPOT TWAP last price capture (sourceCode=%s)".formatted(
                Objects.requireNonNull(sourceCode, "sourceCode must not be null").value()
        ));
    }
}
