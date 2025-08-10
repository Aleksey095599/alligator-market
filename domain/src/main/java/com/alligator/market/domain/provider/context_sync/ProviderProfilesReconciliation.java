package com.alligator.market.domain.provider.context_sync;

import com.alligator.market.domain.provider.profile.model.ProviderProfile;
import com.alligator.market.domain.provider.profile.catalog.ProviderProfileStorage;
import com.alligator.market.domain.provider.profile.model.ProviderProfileStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Сервис, реализующий доменную логику сопоставления профилей провайдеров,
 * извлеченных из контекста приложения и из хранилища данных.
 */
public class ProviderProfilesReconciliation {

    private final ProviderContextScanner contextScanner;
    private final ProviderProfileStorage profileStorage;

    public ProviderProfilesReconciliation(ProviderContextScanner contextScanner,
                                          ProviderProfileStorage profileStorage) {
        this.contextScanner = contextScanner;
        this.profileStorage = profileStorage;
    }

    /**
     * Сравнить профили провайдеров рыночных данных в хранилище данных и извлеченных
     * из контекста приложения.
     *
     * @return {@link ContextDiff}
     */
    public ContextDiff compare() {
        List<ProviderProfile> contextProfiles = contextScanner.getProviderProfiles();
        Map<ProviderProfile, Long> dbActiveProfiles = profileStorage.findAllActive();

        ContextDiff diff = new ContextDiff();
        List<ProviderProfile> restContextProfiles = new ArrayList<>(contextProfiles);

        for (Map.Entry<ProviderProfile, Long> entry : dbActiveProfiles.entrySet()) {
            ProviderProfile dbProfile = entry.getKey();
            Long id = entry.getValue();

            ProviderProfile contextMatch = null;
            for (ProviderProfile p : restContextProfiles) {
                if (p.providerCode().equals(dbProfile.providerCode())) {
                    contextMatch = p;
                    break;
                }
            }

            if (contextMatch == null) {
                diff.putToMissingList(id);
                continue;
            }

            if (contextMatch.equals(dbProfile)) {
                restContextProfiles.remove(contextMatch);
                continue;
            }

            diff.putToReplaceList(id);
            diff.putToAddList(contextMatch);
            restContextProfiles.remove(contextMatch);
        }

        restContextProfiles.forEach(diff::putToAddList);
        return diff;
    }

    /**
     * Применить {@link ContextDiff} к хранилищу данных для синхронизации списка провайдеров
     * рыночных данных с контекстом приложения.
     */
    public void applyContextDiffToStorage(ContextDiff diff) {
        if (!diff.add().isEmpty()) {
            profileStorage.saveAll(diff.add());
        }
        if (!diff.replaced().isEmpty()) {
            profileStorage.updateStatus(diff.replaced(), ProviderProfileStatus.REPLACED);
        }
        if (!diff.missing().isEmpty()) {
            profileStorage.updateStatus(diff.missing(), ProviderProfileStatus.MISSING);
        }
    }
}
