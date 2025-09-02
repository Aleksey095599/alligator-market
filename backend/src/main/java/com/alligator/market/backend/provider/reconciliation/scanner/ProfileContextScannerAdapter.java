package com.alligator.market.backend.provider.reconciliation.scanner;

import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.provider.profile.reconciliation.ProfileContextScanner;
import com.alligator.market.domain.provider.profile.model.Profile;
import com.alligator.market.domain.provider.profile.service.ProfileValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Компонент реализует доменный контракт сканера контекста приложения:
 * 1) ищет в контексте Spring все адаптеры провайдеров рыночных данных, реализующих контракт {@link MarketDataProvider};
 * 2) проверяет, что в контексте нет дублей провайдеров по кодам и именам.
 */
@Component
@RequiredArgsConstructor
public class ProfileContextScannerAdapter implements ProfileContextScanner {

    // Список всех адаптеров провайдеров
    private final List<MarketDataProvider> providers;

    // Доменный сервис проверки профилей
    private static final ProfileValidator profileValidator = new ProfileValidator();

    /** Возвращает список профилей провайдеров. */
    @Override
    public List<Profile> getProfiles() {

        List<Profile> profiles = providers.stream()
                .map(MarketDataProvider::getProfile)
                .toList();

        // Проверка на дублирование по кодам и именам провайдеров
        profileValidator.validateNoDuplicates(profiles);

        return profiles;
    }
}
