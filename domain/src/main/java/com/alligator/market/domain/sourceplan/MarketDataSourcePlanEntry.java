package com.alligator.market.domain.sourceplan;

import com.alligator.market.domain.source.vo.MarketDataSourceCode;

import java.util.Objects;

/**
 * An entry in a market data source plan.
 *
 * @param sourceCode   the market data source code
 * @param priority     the entry priority; lower values have higher priority, with 0 as the highest priority
 */

public record MarketDataSourcePlanEntry(
        MarketDataSourceCode sourceCode,
        int priority
) {
    public MarketDataSourcePlanEntry {
        Objects.requireNonNull(sourceCode, "sourceCode must not be null");

        if (priority < 0) {
            throw new IllegalArgumentException("priority must be greater than or equal to 0");
        }
    }
}
