package com.alligator.market.backend.provider.profile.context_sync;

import com.alligator.market.domain.provider.MarketDataProvider;
import com.alligator.market.domain.provider.context_sync.ProviderContextScanner;
import com.alligator.market.domain.provider.profile.ProviderProfile;
import com.alligator.market.domain.provider.context_sync.DuplicateProviderProfileInContextException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Компонент реализует порт для сканирования контекста приложения и извлечения профилей провайдеров рыночных данных:
 * ищет в контексте Spring все адаптеры провайдеров реализующих контракт {@link MarketDataProvider}
 * и извлекает соответствующие им профили провайдеров.
 */
@Component
@RequiredArgsConstructor
public class ProviderContextScannerAdapter implements ProviderContextScanner {

    /** Список всех адаптеров провайдеров. */
    private final List<MarketDataProvider> providers;

    /** Возвращает список профилей провайдеров. */
    @Override
    public List<ProviderProfile> getProviderProfiles() {
        List<ProviderProfile> profiles = providers.stream()
                .map(MarketDataProvider::profile)
                .toList();

        // Проверка на дублирование по кодам и именам провайдеров
        validateNoDuplicates(profiles);

        return profiles;
    }

    /** Проверяет, что <code>providerCode</code> и <code>displayName</code> уникальны. */
    private void validateNoDuplicates(List<ProviderProfile> profiles) {
        java.util.Set<String> codes = new java.util.HashSet<>();
        java.util.Set<String> names = new java.util.HashSet<>();

        for (ProviderProfile profile : profiles) {
            if (!codes.add(profile.providerCode())) {
                throw new DuplicateProviderProfileInContextException("providerCode", profile.providerCode());
            }
            if (!names.add(profile.displayName())) {
                throw new DuplicateProviderProfileInContextException("displayName", profile.displayName());
            }
        }
    }
}
