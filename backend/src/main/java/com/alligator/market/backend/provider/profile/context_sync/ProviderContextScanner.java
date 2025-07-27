package com.alligator.market.backend.provider.profile.context_sync;

import com.alligator.market.domain.provider.MarketDataProvider;
import com.alligator.market.domain.provider.profile.ProviderProfile;
import com.alligator.market.backend.provider.profile.exception.DuplicateProviderProfileException;
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
        List<ProviderProfile> profiles = providers.stream()
                .map(MarketDataProvider::profile)
                .toList();

        validateNoDuplicates(profiles);

        return profiles;
    }

    /** Проверяем, что providerCode и displayName уникальны. */
    private void validateNoDuplicates(List<ProviderProfile> profiles) {
        java.util.Set<String> codes = new java.util.HashSet<>();
        java.util.Set<String> names = new java.util.HashSet<>();

        for (ProviderProfile profile : profiles) {
            if (!codes.add(profile.providerCode())) {
                throw new DuplicateProviderProfileException("providerCode", profile.providerCode());
            }
            if (!names.add(profile.displayName())) {
                throw new DuplicateProviderProfileException("displayName", profile.displayName());
            }
        }
    }
}
