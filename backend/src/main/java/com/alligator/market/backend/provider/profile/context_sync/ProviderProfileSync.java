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
        // Извлекаем профили из таблицы вместе с их PK
        Map<ProviderProfile, Long> dbProfiles = profileService.findAll();

        Map<String, ProviderProfile> contextMap = contextProfiles.stream()
                .collect(java.util.stream.Collectors.toMap(ProviderProfile::providerCode, p -> p));

        List<ProviderProfile> same = new ArrayList<>();
        List<ProviderProfile> changed = new ArrayList<>();
        List<ProviderProfile> onlyDb = new ArrayList<>();
        List<ProviderProfile> onlyContext = new ArrayList<>();

        for (Map.Entry<ProviderProfile, Long> entry : dbProfiles.entrySet()) {
            ProviderProfile dbProfile = entry.getKey();
            ProviderProfile contextProfile = contextMap.remove(dbProfile.providerCode());
            if (contextProfile == null) {
                onlyDb.add(dbProfile);
            } else if (contextProfile.equals(dbProfile)) {
                same.add(dbProfile);
            } else {
                changed.add(dbProfile);
            }
        }

        onlyContext.addAll(contextMap.values());

        return new SyncResult(same, changed, onlyDb, onlyContext);
    }

    /**
     * Результат сравнения профилей.
     */
    public record SyncResult(
            List<ProviderProfile> same,
            List<ProviderProfile> changed,
            List<ProviderProfile> onlyDb,
            List<ProviderProfile> onlyContext
    ) {}
}
