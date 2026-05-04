package com.alligator.market.backend.marketdata.capture.process.catalog.twap.analytical.lastprice.application.exception;

import com.alligator.market.domain.provider.vo.ProviderCode;

import java.util.Objects;

/**
 * Ошибка application-слоя: провайдер из плана источников отсутствует в реестре.
 */
public final class AnalyticalTwapLastPriceProviderNotFoundException extends IllegalStateException {

    public AnalyticalTwapLastPriceProviderNotFoundException(ProviderCode providerCode) {
        super("Provider not found for analytical TWAP last price capture (providerCode=%s)".formatted(
                Objects.requireNonNull(providerCode, "providerCode must not be null").value()
        ));
    }
}
