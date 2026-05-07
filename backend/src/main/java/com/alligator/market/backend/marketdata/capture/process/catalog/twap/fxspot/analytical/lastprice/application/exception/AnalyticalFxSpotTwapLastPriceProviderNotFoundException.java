package com.alligator.market.backend.marketdata.capture.process.catalog.twap.fxspot.analytical.lastprice.application.exception;

import com.alligator.market.domain.source.vo.ProviderCode;

import java.util.Objects;

/**
 * Ошибка application-слоя: провайдер из плана источников отсутствует в реестре.
 */
public final class AnalyticalFxSpotTwapLastPriceProviderNotFoundException extends IllegalStateException {

    public AnalyticalFxSpotTwapLastPriceProviderNotFoundException(ProviderCode providerCode) {
        super("Provider not found for analytical FX_SPOT TWAP last price capture (providerCode=%s)".formatted(
                Objects.requireNonNull(providerCode, "providerCode must not be null").value()
        ));
    }
}
