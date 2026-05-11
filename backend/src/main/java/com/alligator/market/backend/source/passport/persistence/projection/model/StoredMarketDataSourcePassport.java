package com.alligator.market.backend.source.passport.persistence.projection.model;

import com.alligator.market.domain.source.passport.MarketDataSourcePassport;
import com.alligator.market.domain.source.vo.MarketDataSourceCode;

import java.util.Objects;

public record StoredMarketDataSourcePassport(
        MarketDataSourceCode sourceCode,
        MarketDataSourcePassport passport,
        MarketDataSourceProjectionLifecycleStatus lifecycleStatus
) {
    public StoredMarketDataSourcePassport {
        Objects.requireNonNull(sourceCode, "sourceCode must not be null");
        Objects.requireNonNull(passport, "passport must not be null");
        Objects.requireNonNull(lifecycleStatus, "lifecycleStatus must not be null");
    }
}
