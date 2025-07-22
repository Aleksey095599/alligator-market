package com.alligator.market.backend.provider.catalog.service;

import com.alligator.market.domain.provider.MarketDataProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Компонент для доступа ко всем зарегистрированным провайдерам котировок.
 */
@Component
@RequiredArgsConstructor
public class ProviderContextScanner {

    /** Список всех контрактов адаптеров*/
    private final List<MarketDataProvider> providers;

    /** Возвращает неизменяемый список всех обнаруженных провайдеров. */
    public List<MarketDataProvider> findAll() {
        return List.copyOf(providers);
    }
}
