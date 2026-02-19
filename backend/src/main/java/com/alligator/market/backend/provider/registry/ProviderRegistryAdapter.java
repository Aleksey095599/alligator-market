package com.alligator.market.backend.provider.registry;

import com.alligator.market.domain.provider.model.MarketDataProvider;
import com.alligator.market.domain.provider.registry.AbstractProviderRegistry;

import java.util.List;
import java.util.Objects;


/**
 * Адаптер доменного {@link AbstractProviderRegistry}.
 */
public final class ProviderRegistryAdapter extends AbstractProviderRegistry {

    //=================================================================================================================
    // ВХОДНЫЕ ДАННЫЕ И КОНСТРУКТОР
    //=================================================================================================================

    /* Зарегистрированные в приложении провайдеры. */
    private final List<MarketDataProvider> providers;

    /* Конструктор: фиксируем список провайдеров и валидируем базовые предпосылки. */
    public ProviderRegistryAdapter(List<MarketDataProvider> providers) {
        this.providers = List.copyOf(Objects.requireNonNull(providers, "providers must not be null"));

        if (providers.isEmpty()) {
            throw new IllegalArgumentException("providers must not be empty");
        }
    }

    //=================================================================================================================
    // РЕАЛИЗАЦИЯ ТОЧЕК РАСШИРЕНИЯ БАЗОВОГО КЛАССА
    //=================================================================================================================

    /**
     * Возвращает последовательность провайдеров (источник задаётся реализацией).
     */
    @Override
    protected Iterable<MarketDataProvider> providers() {
        return providers;
    }
}
