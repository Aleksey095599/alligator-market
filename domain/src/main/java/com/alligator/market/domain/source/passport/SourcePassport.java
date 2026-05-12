package com.alligator.market.domain.source.passport;

import com.alligator.market.domain.source.passport.classification.AccessMethod;
import com.alligator.market.domain.source.passport.classification.DeliveryMode;
import com.alligator.market.domain.source.passport.vo.SourceDisplayName;

import java.util.Objects;

public record SourcePassport(
        SourceDisplayName displayName,
        DeliveryMode deliveryMode,
        AccessMethod accessMethod,
        boolean bulkSubscription
) {

    public SourcePassport {
        Objects.requireNonNull(displayName, "displayName must not be null");
        Objects.requireNonNull(deliveryMode, "deliveryMode must not be null");
        Objects.requireNonNull(accessMethod, "accessMethod must not be null");
    }
}
