package com.alligator.market.backend.provider.profile.sync;

import com.alligator.market.domain.provider.MarketDataProvider;
import com.alligator.market.domain.provider.ProviderProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Компонент для сканирования всех профилей провайдеров рыночных данных.
 */
@Component
@RequiredArgsConstructor
public class ProviderContextScanner {

    /** Список всех адаптеров провайдеров */
    private final List<MarketDataProvider> providers;

    /** Возвращает список профилей, доступных в контексте */
    public List<ProviderProfile> findAll() {
        return providers.stream()
                .map(MarketDataProvider::profile)
                .toList();
    }
}
