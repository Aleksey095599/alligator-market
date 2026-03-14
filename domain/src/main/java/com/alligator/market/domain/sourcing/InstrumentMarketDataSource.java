package com.alligator.market.domain.sourcing;

import com.alligator.market.domain.provider.model.vo.ProviderCode;

import java.util.Objects;

/**
 * Источник рыночных данных для конкретного инструмента.
 *
 * @param providerCode Код провайдера, который является источником рыночных данных
 * @param active       Признак активности источника
 */
public record InstrumentMarketDataSource(
        ProviderCode providerCode,
        boolean active
) {
    public InstrumentMarketDataSource {
        Objects.requireNonNull(providerCode, "providerCode must not be null");
    }
}
