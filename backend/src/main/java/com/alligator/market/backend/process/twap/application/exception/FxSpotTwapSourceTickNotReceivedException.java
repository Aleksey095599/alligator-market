package com.alligator.market.backend.process.twap.application.exception;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.source.vo.MarketDataSourceCode;

import java.util.Objects;

public final class FxSpotTwapSourceTickNotReceivedException extends IllegalStateException {
    public FxSpotTwapSourceTickNotReceivedException(
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
