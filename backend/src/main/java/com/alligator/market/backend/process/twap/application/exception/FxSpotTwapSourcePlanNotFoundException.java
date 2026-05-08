package com.alligator.market.backend.process.twap.application.exception;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.capturer.vo.MarketDataCapturerCode;

import java.util.Objects;

public final class FxSpotTwapSourcePlanNotFoundException extends IllegalStateException {
    public FxSpotTwapSourcePlanNotFoundException(
            MarketDataCapturerCode capturerCode,
            InstrumentCode instrumentCode
    ) {
        super("Source plan not found for analytical FX_SPOT TWAP last price capture "
                + "(capturerCode=%s, instrumentCode=%s)".formatted(
                Objects.requireNonNull(capturerCode, "capturerCode must not be null").value(),
                Objects.requireNonNull(instrumentCode, "instrumentCode must not be null").value()
        ));
    }
}
