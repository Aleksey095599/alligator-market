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

    /* Список зарегистрированных в приложении провайдеры. */
    private final List<MarketDataProvider> providerList;

    /* Конструктор: фиксируем список провайдеров и валидируем базовые предпосылки. */
    public ProviderRegistryAdapter(List<MarketDataProvider> providerList) {
        this.providerList = List.copyOf(Objects.requireNonNull(providerList, "providers must not be null"));

        if (providerList.isEmpty()) {
            throw new IllegalArgumentException("providers must not be empty");
        }
    }

    //=================================================================================================================
    // РЕАЛИЗАЦИЯ ТОЧЕК РАСШИРЕНИЯ БАЗОВОГО КЛАССА
    //=================================================================================================================

    /**
     * Источник провайдеров для построения доменного реестра.
     */
    @Override
    protected Iterable<MarketDataProvider> providers() {
        return providerList;
    }
}
