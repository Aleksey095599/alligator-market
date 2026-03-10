package com.alligator.market.domain.routing.model;

import com.alligator.market.domain.instrument.base.model.vo.InstrumentCode;
import com.alligator.market.domain.provider.model.vo.ProviderCode;

import java.util.Objects;

/**
 * Доменная модель маршрута рыночных данных: определяет, через какого провайдера запрашиваются данные для конкретного инструмента.
 *
 * TODO: может path?
 */
public record MarketDataRoute(
        InstrumentCode instrumentCode,
        ProviderCode providerCode
) {
    public MarketDataRoute {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(providerCode, "providerCode must not be null");
    }
}
