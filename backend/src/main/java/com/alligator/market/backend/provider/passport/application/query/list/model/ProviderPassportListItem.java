package com.alligator.market.backend.provider.passport.application.query.list.model;

import java.util.Objects;

/**
 * Read model for one provider passport list item.
 */
public record ProviderPassportListItem(
        String providerCode,
        String displayName,
        String deliveryMode,
        String accessMethod,
        boolean bulkSubscription,
        String lifecycleStatus
) {

    public ProviderPassportListItem {
        Objects.requireNonNull(providerCode, "providerCode must not be null");
        Objects.requireNonNull(displayName, "displayName must not be null");
        Objects.requireNonNull(deliveryMode, "deliveryMode must not be null");
        Objects.requireNonNull(accessMethod, "accessMethod must not be null");
        Objects.requireNonNull(lifecycleStatus, "lifecycleStatus must not be null");
    }
}
