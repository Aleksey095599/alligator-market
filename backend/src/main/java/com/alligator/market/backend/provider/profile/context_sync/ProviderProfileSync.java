package com.alligator.market.backend.provider.profile.context_sync;

import com.alligator.market.backend.provider.profile.service.ProviderProfileService;
import com.alligator.market.domain.provider.profile.ProviderProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Синхронизация профилей провайдеров из контекста и базы данных.
 */
@Component
@RequiredArgsConstructor
public class ProviderProfileSync {

    private final ProviderContextScanner contextScanner;
    private final ProviderProfileService profileService;

    /**
     * Сравнивает профили провайдеров из контекста и базы данных.
     */
    public SyncResult compare() {

        // Извлекаем профили из контекста
        List<ProviderProfile> contextProfiles = contextScanner.getProviderProfiles();

        // Проверяем, что профили из контекста имеют разные providerCode и displayName


        // Извлекаем профили из таблицы вместе с PK
        Map<ProviderProfile, Long> dbProfiles = profileService.findAll();

    }

    /**
     * Результат сравнения профилей.
     */
    public record SyncResult(
            Map<ProviderProfile, Long> addNewWithActiveStatus,
            Map<ProviderProfile, Long> changeStatusToReplaced,
            Map<ProviderProfile, Long> changeStatusToMissing
    ) {}
}
