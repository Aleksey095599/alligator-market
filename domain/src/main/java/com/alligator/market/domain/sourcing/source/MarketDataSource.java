package com.alligator.market.domain.sourcing.source;

import com.alligator.market.domain.provider.vo.ProviderCode;

import java.util.Objects;

/**
 * Market data source selected in a source plan.
 *
 * @param providerCode provider that supplies market data
 * @param priority source priority; lower value means higher priority
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
