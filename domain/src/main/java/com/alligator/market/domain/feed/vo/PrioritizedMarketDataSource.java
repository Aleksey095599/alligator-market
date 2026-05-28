package com.alligator.market.domain.feed.vo;

import com.alligator.market.domain.source.MarketDataSource;

import java.util.Objects;

public record PrioritizedMarketDataSource(
        MarketDataSource source,
        int priority
) {
    public PrioritizedMarketDataSource {
        Objects.requireNonNull(source, "source must not be null");
        Objects.requireNonNull(source.code(), "source.code must not be null");

        if (priority < 0) {
            throw new IllegalArgumentException("priority must be greater than or equal to 0");
        }
    }
}
