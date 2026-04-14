package com.alligator.market.backend.instrument.asset.forex.reference.currency.application.usage.contributor.fxspot;

import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.usage.contributor.CurrencyUsageContributor;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;
import com.alligator.market.domain.instrument.asset.forex.fxspot.repository.FxSpotRepository;

import java.util.Objects;

/**
 * Contributor проверки использования валюты в инструментах FxSpot.
 */
public final class FxSpotCurrencyUsageContributor implements CurrencyUsageContributor {

    private final FxSpotRepository fxSpotRepository;

    public FxSpotCurrencyUsageContributor(FxSpotRepository fxSpotRepository) {
        this.fxSpotRepository = Objects.requireNonNull(fxSpotRepository, "fxSpotRepository must not be null");
    }

    @Override
    public boolean isUsed(CurrencyCode currencyCode) {
        Objects.requireNonNull(currencyCode, "currencyCode must not be null");

        return fxSpotRepository.existsByCurrencyCode(currencyCode);
    }
}
