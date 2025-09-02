package com.alligator.market.backend.provider.reconciliation.scanner;

import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.provider.reconciliation.ProfileContextScanner;
import com.alligator.market.domain.provider.profile.model.Profile;
import com.alligator.market.domain.provider.profile.service.ProfileValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    /** Возвращает карту профилей провайдеров. */
    @Override
    public Map<String, Profile> getProfiles() {

        List<Profile> profiles = providers.stream()
                .map(MarketDataProvider::getProfile)
                .toList();

        // Проверка на дублирование по кодам и именам провайдеров
        profileValidator.validateNoDuplicates(profiles);

        // Преобразуем список в Map по коду провайдера
        return profiles.stream()
                .collect(Collectors.toMap(Profile::providerCode, Function.identity()));
    }
}
