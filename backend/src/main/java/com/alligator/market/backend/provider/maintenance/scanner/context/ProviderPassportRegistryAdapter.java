package com.alligator.market.backend.provider.maintenance.scanner.context;

import com.alligator.market.domain.provider.model.MarketDataProvider;
import com.alligator.market.domain.provider.registry.passport.AbstractProviderPassportRegistry;

import java.util.List;
import java.util.Objects;

/**
 * Адаптер доменного {@link AbstractProviderPassportRegistry}.
 */
public class ProviderPassportRegistryAdapter extends AbstractProviderPassportRegistry {

    /* Список провайдеров. */
    private final List<MarketDataProvider> providers;

    /* Конструктор. */
    public ProviderPassportRegistryAdapter(List<MarketDataProvider> providers) {
        this.providers = Objects.requireNonNull(providers, "providers must not be null");
    }

    @Override
    protected Iterable<MarketDataProvider> providers() {
        return providers;
    }
}
