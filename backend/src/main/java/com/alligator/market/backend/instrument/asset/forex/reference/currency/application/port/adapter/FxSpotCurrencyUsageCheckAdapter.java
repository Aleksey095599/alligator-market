package com.alligator.market.backend.instrument.asset.forex.reference.currency.application.port.adapter;

import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.port.CurrencyUsageCheckPort;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.model.vo.CurrencyCode;
import com.alligator.market.domain.instrument.asset.forex.spot.repository.FxSpotRepository;

import java.util.Objects;

/**
 * Адаптер порта проверки использования валюты в FX Spot инструментах.
 */
public final class FxSpotCurrencyUsageCheckAdapter implements CurrencyUsageCheckPort {

    /* Репозиторий FX Spot для проверки использования валюты. */
    private final FxSpotRepository fxSpotRepository;

    public FxSpotCurrencyUsageCheckAdapter(FxSpotRepository fxSpotRepository) {
        this.fxSpotRepository = Objects.requireNonNull(fxSpotRepository, "fxSpotRepository must not be null");
    }

    @Override
    public boolean isUsed(CurrencyCode currencyCode) {
        // Проверяем обязательный входной аргумент.
        Objects.requireNonNull(currencyCode, "currencyCode must not be null");

        return fxSpotRepository.existsByCurrencyCode(currencyCode);
    }
}
