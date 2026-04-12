package com.alligator.market.backend.instrument.asset.forex.reference.currency.application.contributor.spot;

import com.alligator.market.domain.instrument.asset.forex.reference.currency.model.vo.CurrencyCode;
import com.alligator.market.domain.instrument.asset.forex.spot.repository.FxSpotRepository;

import java.util.Objects;

/**
 * Contributor проверки использования валюты в инструментах FX Spot.
 */
public final class FxSpotCurrencyUsageContributor implements CurrencyUsageContributor {

    /* Репозиторий FX Spot для проверки использования валюты. */
    private final FxSpotRepository fxSpotRepository;

    public FxSpotCurrencyUsageContributor(FxSpotRepository fxSpotRepository) {
        this.fxSpotRepository = Objects.requireNonNull(fxSpotRepository, "fxSpotRepository must not be null");
    }

    @Override
    public boolean isUsed(CurrencyCode currencyCode) {
        // Проверяем обязательный входной аргумент.
        Objects.requireNonNull(currencyCode, "currencyCode must not be null");

        return fxSpotRepository.existsByCurrencyCode(currencyCode);
    }
}
