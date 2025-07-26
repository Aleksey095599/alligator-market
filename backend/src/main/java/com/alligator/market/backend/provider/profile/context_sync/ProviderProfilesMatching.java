package com.alligator.market.backend.provider.profile.context_sync;

import com.alligator.market.backend.provider.profile.service.ProviderProfileService;
import com.alligator.market.backend.provider.profile.exception.DuplicateProviderProfileException;
import com.alligator.market.domain.provider.profile.ProviderProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Компонент сравнивает профили провайдеров, извлеченных из контекста Spring и извлеченных из базы данных,
 * и возвращает набор профилей {@link CompareResult} для дальнейших действий в целях синхронизации.
 */
@Component
@RequiredArgsConstructor
public class ProviderProfilesMatching {

    private final ProviderContextScanner contextScanner;
    private final ProviderProfileService profileService;

    public CompareResult compare() {

        // Извлекаем профили из контекста
        List<ProviderProfile> contextProfiles = contextScanner.getProviderProfiles();

        // Проверяем, что профили из контекста имеют разные providerCode и displayName
        Set<String> codes = new HashSet<>();
        Set<String> names = new HashSet<>();
        for (ProviderProfile profile : contextProfiles) {
            if (!codes.add(profile.providerCode())) {
                throw new DuplicateProviderProfileException("providerCode", profile.providerCode());
            }
            if (!names.add(profile.displayName())) {
                throw new DuplicateProviderProfileException("displayName", profile.displayName());
            }

        }

        // Извлекаем активные профили из таблицы вместе с PK
        Map<ProviderProfile, Long> dbProfiles = profileService.findAllActive();

    }
}
