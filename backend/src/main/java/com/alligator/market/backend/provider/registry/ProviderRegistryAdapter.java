package com.alligator.market.backend.provider.registry;

import com.alligator.market.domain.provider.model.MarketDataProvider;
import com.alligator.market.domain.provider.registry.AbstractProviderRegistry;

import java.util.List;
import java.util.Objects;


/**
 * Адаптер доменного {@link AbstractProviderRegistry}.
 *
 * <p>Источник провайдеров — Spring-контекст (внедряется как список бинов {@link MarketDataProvider}).</p>
 */
public final class ProviderRegistryAdapter extends AbstractProviderRegistry {

    /* Провайдеры из DI-контейнера (порядок задаётся Spring: @Order / Ordered). */
    private final List<MarketDataProvider> providers;

    /* Конструктор. */
    public ProviderRegistryAdapter(List<MarketDataProvider> providers) {
        this.providers = List.copyOf(Objects.requireNonNull(providers, "providers must not be null"));
    }

    @Override
    protected Iterable<MarketDataProvider> providers() {
        return providers;
    }
}
