package com.alligator.market.domain.provider.reconciliation;

import com.alligator.market.domain.provider.profile.model.Profile;
import com.alligator.market.domain.provider.profile.model.ProfileStatus;
import com.alligator.market.domain.provider.profile.repository.ProfileRepository;
import com.alligator.market.domain.provider.reconciliation.ProviderContextScanner;

import java.util.Map;

/**
 * Доменный сервис сопоставления профилей провайдеров.
 */
public final class ProfileReconciler {

    // Сканер контекста провайдеров
    private final ProviderContextScanner providerContextScanner;
    private final ProfileRepository repository;

    // Конструктор
    public ProfileReconciler(ProviderContextScanner providerContextScanner,
                             ProfileRepository repository) {
        this.providerContextScanner = providerContextScanner;
        this.repository = repository;
    }

    /**
     * Сравнить профили в контексте и репозитории.
     *
     * @return {@link ProfileDiff}
     */
    public ProfileDiff compareContextAndRepository() {

        ProfileDiff diff = new ProfileDiff();

        Map<String, Profile> contextByCode = providerContextScanner.getProfiles();

        repository.findAllActive().forEach((id, repoProfile) -> {
            Profile contextProfile = contextByCode.remove(repoProfile.providerCode());
            if (contextProfile == null) {
                diff.putToMissingList(id);
            } else if (!contextProfile.equals(repoProfile)) {
                diff.putToReplaceList(id);
                diff.putToAddList(contextProfile);
            }
        });

        contextByCode.values().forEach(diff::putToAddList);
        return diff;
    }

    /**
     * Применить {@link ProfileDiff} к репозиторию.
     */
    public void applyDiffToRepository(ProfileDiff diff) {
        if (!diff.add().isEmpty()) {
            repository.saveAll(diff.add());
        }
        if (!diff.replaced().isEmpty()) {
            repository.updateStatus(diff.replaced(), ProfileStatus.REPLACED);
        }
        if (!diff.missing().isEmpty()) {
            repository.updateStatus(diff.missing(), ProfileStatus.MISSING);
        }
    }
}

