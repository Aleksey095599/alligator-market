package com.alligator.market.domain.provider.sync.contract;

import com.alligator.market.domain.provider.contract.MarketDataProvider;

import java.util.List;

/**
 * Контракт сканера контекста приложения, извлекающего профили провайдеров рыночных данных.
 */
public interface ProviderContextScanner {

    /**
     * Вернуть список всех провайдеров рыночных данных из контекста.
     */
    List<MarketDataProvider> getProviders();
}
