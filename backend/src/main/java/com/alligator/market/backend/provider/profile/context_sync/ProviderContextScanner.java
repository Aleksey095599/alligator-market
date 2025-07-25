package com.alligator.market.backend.provider.profile.context_sync;

import com.alligator.market.domain.provider.MarketDataProvider;
import com.alligator.market.domain.provider.profile.ProviderProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Компонент для сканирования всех бинов адаптеров провайдеров реализующих контракт {@link MarketDataProvider}.
 * Каждому бину адаптера провайдера, согласно контракту, соответствует свой профиль провайдера.
 * Данный компонент содержит метод, который извлекает указанные профили провайдера в виде списка.
 */
@Component
@RequiredArgsConstructor
public class ProviderContextScanner {

    /** Список всех адаптеров провайдеров */
    private final List<MarketDataProvider> providers;

    /** Возвращает список профилей провайдеров */
    public List<ProviderProfile> getProviderProfiles() {
        return providers.stream()
                .map(MarketDataProvider::profile)
                .toList();
    }
}
