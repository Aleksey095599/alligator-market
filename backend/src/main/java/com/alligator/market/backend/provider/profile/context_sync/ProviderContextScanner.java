package com.alligator.market.backend.provider.profile.context_sync;

import com.alligator.market.domain.provider.MarketDataProvider;
import com.alligator.market.domain.provider.profile.ProviderProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Компонент ищет в контексте Spring все адаптеры провайдеров реализующих контракт {@link MarketDataProvider}
 * и извлекает соответствующие им профили провайдеров.
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
