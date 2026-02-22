package com.alligator.market.backend.provider.registry;

import com.alligator.market.domain.provider.model.MarketDataProvider;
import com.alligator.market.domain.provider.model.passport.ProviderPassport;
import com.alligator.market.domain.provider.registry.ProviderRegistry;
import com.alligator.market.domain.provider.registry.SnapshotProviderRegistry;
import com.alligator.market.domain.provider.model.vo.ProviderCode;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Backend-адаптер доменного реестра провайдеров {@link ProviderRegistry}.
 *
 * <p>Назначение: принять spring-список провайдеров {@link MarketDataProvider} и вернуть {@link SnapshotProviderRegistry}.</p>
 */
public final class ProviderRegistryAdapter implements ProviderRegistry {

    /* Доменный реестр провайдеров. */
    private final ProviderRegistry registry;

    /**
     * Конструктор: фиксируем список провайдеров, строим доменный {@link SnapshotProviderRegistry} и передаем его
     * полю {@link #registry}.
     *
     * @param providerList список провайдеров
     */
    public ProviderRegistryAdapter(List<MarketDataProvider> providerList) {
        Objects.requireNonNull(providerList, "providers must not be null");

        if (providerList.isEmpty()) {
            throw new IllegalArgumentException("providers must not be empty");
        }

        // Фиксируем входные данные (защита от внешних модификаций списка)
        List<MarketDataProvider> providerListCopy = List.copyOf(providerList);

        // Строим доменный snapshot-реестр и передаем его полю registry
        this.registry = new SnapshotProviderRegistry(providerListCopy);
    }

    @Override
    public Map<ProviderCode, MarketDataProvider> providersByCode() {
        return registry.providersByCode();
    }

    @Override
    public Map<ProviderCode, ProviderPassport> passportsByCode() {
        return registry.passportsByCode();
    }
}
