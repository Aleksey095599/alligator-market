package com.alligator.market.backend.provider.profile.service;

import com.alligator.market.domain.provider.MarketDataProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Компонент для сканирования всех профилей провайдеров рыночных данных.
 */
@Component
@RequiredArgsConstructor
public class ProviderContextScanner {

    /** Список всех профилей провайдеров */
    private final List<MarketDataProvider> providers;

    /** Возвращает неизменяемый список всех профилей провайдеров */
    public List<MarketDataProvider> findAll() {
        return List.copyOf(providers);
    }
}
