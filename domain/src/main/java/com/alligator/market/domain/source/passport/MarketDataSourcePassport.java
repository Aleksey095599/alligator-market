package com.alligator.market.domain.source.passport;

import com.alligator.market.domain.source.passport.classification.AccessMethod;
import com.alligator.market.domain.source.passport.classification.DeliveryMode;
import com.alligator.market.domain.source.passport.vo.MarketDataSourceDisplayName;

import java.util.Objects;

/**
 * Immutable descriptive metadata of a market data source.
 *
 * @param displayName      the source display name
 * @param deliveryMode     the market data delivery mode
 * @param accessMethod     the market data access method
 * @param bulkSubscription whether the source supports bulk subscription in a single request
 */
public record MarketDataSourcePassport(
        MarketDataSourceDisplayName displayName,
        DeliveryMode deliveryMode,
        AccessMethod accessMethod,
        boolean bulkSubscription
) {

    public MarketDataSourcePassport {
        Objects.requireNonNull(displayName, "displayName must not be null");
        Objects.requireNonNull(deliveryMode, "deliveryMode must not be null");
        Objects.requireNonNull(accessMethod, "accessMethod must not be null");
    }
}
