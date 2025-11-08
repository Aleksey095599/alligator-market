package com.alligator.market.backend.provider.catalog.service;

import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;

import java.time.Duration;
import java.util.Objects;

/**
 * Иммутабельная модель каталога провайдера для backend-слоя приложения.
 */
public record ProviderCatalogItem(
        String providerCode,
        ProviderDescriptor descriptor,
        Duration minUpdateInterval
) {
    public ProviderCatalogItem {
        Objects.requireNonNull(providerCode, "providerCode must not be null");
        Objects.requireNonNull(descriptor, "descriptor must not be null");
        Objects.requireNonNull(minUpdateInterval, "minUpdateInterval must not be null");

        if (providerCode.isBlank()) {
            throw new IllegalArgumentException("providerCode must not be blank");
        }
    }
}
