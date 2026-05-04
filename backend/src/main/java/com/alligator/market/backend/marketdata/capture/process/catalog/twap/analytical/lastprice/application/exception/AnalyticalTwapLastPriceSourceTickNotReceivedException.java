package com.alligator.market.backend.marketdata.capture.process.catalog.twap.analytical.lastprice.application.exception;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.provider.vo.ProviderCode;

import java.util.Objects;

/**
 * Ошибка application-слоя: провайдер не вернул source tick для одного capture шага.
 */
public final class AnalyticalTwapLastPriceSourceTickNotReceivedException extends IllegalStateException {

    public AnalyticalTwapLastPriceSourceTickNotReceivedException(
            InstrumentCode instrumentCode,
            ProviderCode providerCode
    ) {
        super("Source tick was not received for analytical TWAP last price capture "
                + "(instrumentCode=%s, providerCode=%s)".formatted(
                Objects.requireNonNull(instrumentCode, "instrumentCode must not be null").value(),
                Objects.requireNonNull(providerCode, "providerCode must not be null").value()
        ));
    }
}
