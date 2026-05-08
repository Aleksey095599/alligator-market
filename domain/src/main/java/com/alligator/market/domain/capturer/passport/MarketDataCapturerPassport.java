package com.alligator.market.domain.capturer.passport;

import com.alligator.market.domain.capturer.vo.MarketDataCapturerDisplayName;

import java.util.Objects;

public record MarketDataCapturerPassport(
        MarketDataCapturerDisplayName displayName
) {

    public MarketDataCapturerPassport {
        Objects.requireNonNull(displayName, "displayName must not be null");
    }
}
