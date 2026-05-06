package com.alligator.market.backend.sourcing.plan.application.query.common.model;

import java.util.Objects;

/**
 * Read-модель одной строки source внутри source plan.
 *
 * <p>{@code lifecycleStatus} — технический статус пригодности, а не ручной enable/disable flag.</p>
 */
public record MarketDataSourceQueryItem(
        String providerCode,
        int priority,
        String lifecycleStatus
) {

    public MarketDataSourceQueryItem {
        Objects.requireNonNull(providerCode, "providerCode must not be null");
        Objects.requireNonNull(lifecycleStatus, "lifecycleStatus must not be null");

        if (priority < 0) {
            throw new IllegalArgumentException("priority must be greater than or equal to 0");
        }
    }
}
