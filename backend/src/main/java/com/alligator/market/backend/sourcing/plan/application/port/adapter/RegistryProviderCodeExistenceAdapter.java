package com.alligator.market.backend.sourcing.plan.application.port.adapter;

import com.alligator.market.backend.sourcing.plan.application.port.ProviderCodeExistencePort;
import com.alligator.market.domain.provider.registry.ProviderRegistry;
import com.alligator.market.domain.provider.vo.ProviderCode;

import java.util.Objects;

/**
 * Адаптер порта проверки существования провайдера через доменный реестр провайдеров.
 */
public final class RegistryProviderCodeExistenceAdapter implements ProviderCodeExistencePort {

    /* Доменный реестр активных провайдеров. */
    private final ProviderRegistry providerRegistry;

    public RegistryProviderCodeExistenceAdapter(ProviderRegistry providerRegistry) {
        this.providerRegistry = Objects.requireNonNull(providerRegistry, "providerRegistry must not be null");
    }

    @Override
    public boolean existsByCode(ProviderCode providerCode) {
        Objects.requireNonNull(providerCode, "providerCode must not be null");

        return providerRegistry.providersByCode().containsKey(providerCode);
    }
}
