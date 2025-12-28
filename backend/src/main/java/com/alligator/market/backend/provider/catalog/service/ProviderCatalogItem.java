package com.alligator.market.backend.provider.catalog.service;

import com.alligator.market.domain.provider.code.ProviderCode;
import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.contract.policy.ProviderPolicy;

import java.util.Objects;

/**
 * Иммутабельная модель элемента каталога провайдера рыночных данных.
 *
 * <p>Применяется только в backend-слое приложения.</p>
 */
public record ProviderCatalogItem(
        ProviderCode providerCode,
        ProviderDescriptor descriptor,
        ProviderPolicy policy
) {
    public ProviderCatalogItem {
        Objects.requireNonNull(providerCode, "providerCode must not be null");
        Objects.requireNonNull(descriptor, "descriptor must not be null");
        Objects.requireNonNull(policy, "policy must not be null");
    }
}
