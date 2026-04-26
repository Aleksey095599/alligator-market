package com.alligator.market.backend.instrument.asset.forex.reference.currency.application.usage.contributor.fxspot;

import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.usage.contributor.CurrencyUsageContributor;
import com.alligator.market.backend.instrument.asset.forex.fxspot.application.query.usage.port.FxSpotCurrencyUsageQueryPort;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;

import java.util.Objects;

/**
 * Проверка использования валюты для contributor: инструменты FOREX_SPOT.
 */
public final class FxSpotCurrencyUsageContributor implements CurrencyUsageContributor {

    private final FxSpotCurrencyUsageQueryPort fxSpotCurrencyUsageQueryPort;

    public FxSpotCurrencyUsageContributor(FxSpotCurrencyUsageQueryPort fxSpotCurrencyUsageQueryPort) {
        this.fxSpotCurrencyUsageQueryPort = Objects.requireNonNull(
                fxSpotCurrencyUsageQueryPort,
                "fxSpotCurrencyUsageQueryPort must not be null"
        );
    }

    @Override
    public boolean isUsed(CurrencyCode currencyCode) {
        Objects.requireNonNull(currencyCode, "currencyCode must not be null");

        return fxSpotCurrencyUsageQueryPort.existsByCurrencyCode(currencyCode);
    }
}
