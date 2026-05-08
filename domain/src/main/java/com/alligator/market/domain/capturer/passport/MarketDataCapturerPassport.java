package com.alligator.market.domain.capturer.passport;

import com.alligator.market.domain.capturer.vo.MarketDataCapturerDisplayName;

import java.util.Objects;

/**
 * Stable metadata of a market data capturer.
 *
 * @param displayName human-readable capturer name
 */
public record MarketDataCapturerPassport(
        MarketDataCapturerDisplayName displayName
) {

    public MarketDataCapturerPassport {
        Objects.requireNonNull(displayName, "displayName must not be null");
    }
}
