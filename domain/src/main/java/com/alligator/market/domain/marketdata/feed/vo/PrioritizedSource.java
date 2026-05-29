package com.alligator.market.domain.marketdata.feed.vo;

import com.alligator.market.domain.source.MarketDataSource;

import java.util.Objects;

public record PrioritizedSource(
        MarketDataSource source,
        int priority
) {
    public PrioritizedSource {
        Objects.requireNonNull(source, "source must not be null");
        Objects.requireNonNull(source.code(), "source.code must not be null");

        if (priority < 0) {
            throw new IllegalArgumentException("priority must be greater than or equal to 0");
        }
    }
}
