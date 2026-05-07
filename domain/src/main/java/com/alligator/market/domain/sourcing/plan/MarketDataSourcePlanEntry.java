package com.alligator.market.domain.sourcing.plan;

import com.alligator.market.domain.provider.vo.ProviderCode;

import java.util.Objects;

/**
 * An entry in a market data source plan.
 *
 * @param providerCode the market data provider code
 * @param priority     the entry priority; lower values have higher priority, with 0 as the highest priority
 */

public record MarketDataSourcePlanEntry(
        ProviderCode providerCode,
        int priority
) {
    public MarketDataSourcePlanEntry {
        Objects.requireNonNull(providerCode, "providerCode must not be null");

        if (priority < 0) {
            throw new IllegalArgumentException("priority must be greater than or equal to 0");
        }
    }
}
