package com.alligator.market.backend.provider.sync.scanner;

import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.provider.sync.contract.ProfileContextScanner;
import com.alligator.market.domain.provider.profile.model.ProviderProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Компонент реализует доменный контракт сканера контекста приложения:
 * 1) ищет в контексте Spring все адаптеры провайдеров рыночных данных, реализующих контракт {@link MarketDataProvider};
 * 2) извлекает соответствующие им профили провайдеров;
 * 3) проверяет, что в контексте нет дублей провайдеров по кодам и именам.
 */
@Component
@RequiredArgsConstructor
public class ProfileContextScannerAdapter implements ProfileContextScanner {

    /** Список всех адаптеров провайдеров. */
    private final List<MarketDataProvider> providers;

    /** Возвращает список профилей провайдеров. */
    @Override
    public List<ProviderProfile> getProviderProfiles() {

        List<ProviderProfile> profiles = providers.stream()
                .map(MarketDataProvider::profile)
                .toList();

        // Проверка на дублирование по кодам и именам провайдеров
        ProfileContextCheck.validateNoDuplicates(profiles);

        return profiles;
    }
}
