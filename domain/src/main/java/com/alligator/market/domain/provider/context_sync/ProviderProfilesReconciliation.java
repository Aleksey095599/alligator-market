package com.alligator.market.domain.provider.context_sync;

import com.alligator.market.domain.provider.profile.ProviderProfile;
import com.alligator.market.domain.provider.profile.ProviderProfileStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Сервис сопоставления профилей из контекста и хранилища.
 */
public class ProviderProfilesReconciliation {

    private final ProviderContextScanner contextScanner;
    private final ProviderProfileStorage profileStorage;

    public ProviderProfilesReconciliation(ProviderContextScanner contextScanner,
                                          ProviderProfileStorage profileStorage) {
        this.contextScanner = contextScanner;
        this.profileStorage = profileStorage;
    }

    /** Выполняет сравнение профилей и формирует diff. */
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
                diff.putToMissingMap(dbProfile, id);
                continue;
            }

            if (contextMatch.equals(dbProfile)) {
                restContextProfiles.remove(contextMatch);
                continue;
            }

            diff.putToReplaceMap(dbProfile, id);
            diff.putToAddList(contextMatch);
            restContextProfiles.remove(contextMatch);
        }

        restContextProfiles.forEach(diff::putToAddList);
        return diff;
    }
}
