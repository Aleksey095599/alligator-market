package com.alligator.market.domain.sourcing.source;

import com.alligator.market.domain.provider.vo.ProviderCode;

import java.util.Objects;

/**
 * Источник рыночных данных.
 *
 * @param providerCode код провайдера рыночных данных
 * @param priority     приоритет источника (0 наивысший приоритет)
 */
public record MarketDataSource(
        ProviderCode providerCode,
        int priority
) {
    public MarketDataSource {
        Objects.requireNonNull(providerCode, "providerCode must not be null");

        if (priority < 0) {
            throw new IllegalArgumentException("priority must be greater than or equal to 0");
        }
    }
}
