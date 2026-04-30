package com.alligator.market.backend.sourcing.plan.application.query.options.adapter;

import com.alligator.market.backend.sourcing.plan.application.query.options.port.ProviderOptionsQueryPort;
import com.alligator.market.domain.provider.registry.ProviderRegistry;
import com.alligator.market.domain.provider.vo.ProviderCode;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Адаптер порта получения доступных кодов провайдеров через доменный реестр провайдеров.
 */
public final class RegistryProviderOptionsQueryAdapter implements ProviderOptionsQueryPort {

    /* Доменный реестр активных провайдеров. */
    private final ProviderRegistry providerRegistry;

    public RegistryProviderOptionsQueryAdapter(ProviderRegistry providerRegistry) {
        this.providerRegistry = Objects.requireNonNull(providerRegistry, "providerRegistry must not be null");
    }

    @Override
    public List<ProviderCode> findAllProviderCodes() {
        return providerRegistry.providersByCode().keySet().stream()
                .sorted(Comparator.comparing(ProviderCode::value))
                .toList();
    }
}
