package com.alligator.market.backend.instrument.asset.forex.reference.currency.application.usage.contributor.fxspot;

import com.alligator.market.backend.instrument.asset.forex.fxspot.application.query.reference.currency.port.FxSpotCurrencyReferenceQueryPort;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.usage.contributor.CurrencyUsageContributor;
import com.alligator.market.domain.instrument.catalog.forex.reference.currency.vo.CurrencyCode;

import java.util.Objects;

/**
 * Проверка использования валюты для contributor: инструменты FOREX_SPOT.
 */
public final class FxSpotCurrencyUsageContributor implements CurrencyUsageContributor {

    private final FxSpotCurrencyReferenceQueryPort fxSpotCurrencyReferenceQueryPort;

    public FxSpotCurrencyUsageContributor(FxSpotCurrencyReferenceQueryPort fxSpotCurrencyReferenceQueryPort) {
        this.fxSpotCurrencyReferenceQueryPort = Objects.requireNonNull(
                fxSpotCurrencyReferenceQueryPort,
                "fxSpotCurrencyReferenceQueryPort must not be null"
        );
    }

    @Override
    public boolean isUsed(CurrencyCode currencyCode) {
        Objects.requireNonNull(currencyCode, "currencyCode must not be null");

        return fxSpotCurrencyReferenceQueryPort.referencesCurrency(currencyCode);
    }
}
