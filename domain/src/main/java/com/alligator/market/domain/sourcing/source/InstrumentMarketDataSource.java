package com.alligator.market.domain.sourcing.source;

import com.alligator.market.domain.provider.model.vo.ProviderCode;

import java.util.Objects;

/**
 * Источник рыночных данных для конкретного инструмента.
 *
 * @param providerCode Код провайдера, который является источником рыночных данных
 * @param active       Признак активности источника
 * @param priority     Приоритет источника (чем меньше число, тем выше приоритет)
 */
public record InstrumentMarketDataSource(
        ProviderCode providerCode,
        boolean active,
        int priority
) {
    public InstrumentMarketDataSource {
        Objects.requireNonNull(providerCode, "providerCode must not be null");

        if (priority < 0) {
            throw new IllegalArgumentException("priority must be greater than or equal to 0");
        }
    }
}
