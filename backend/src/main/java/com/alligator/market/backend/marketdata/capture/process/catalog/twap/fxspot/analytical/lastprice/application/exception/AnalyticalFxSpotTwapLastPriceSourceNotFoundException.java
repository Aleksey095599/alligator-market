package com.alligator.market.backend.marketdata.capture.process.catalog.twap.fxspot.analytical.lastprice.application.exception;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.capture.process.vo.CaptureProcessCode;

import java.util.Objects;

/**
 * Application-layer error: a source plan contains no market data source.
 */
public final class AnalyticalFxSpotTwapLastPriceSourceNotFoundException extends IllegalStateException {

    public AnalyticalFxSpotTwapLastPriceSourceNotFoundException(
            CaptureProcessCode captureProcessCode,
            InstrumentCode instrumentCode
    ) {
        super("Market data source not found for analytical FX_SPOT TWAP last price capture "
                + "(captureProcessCode=%s, instrumentCode=%s)".formatted(
                Objects.requireNonNull(captureProcessCode, "captureProcessCode must not be null").value(),
                Objects.requireNonNull(instrumentCode, "instrumentCode must not be null").value()
        ));
    }
}
