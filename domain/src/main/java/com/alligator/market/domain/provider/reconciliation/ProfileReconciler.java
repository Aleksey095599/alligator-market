package com.alligator.market.domain.provider.reconciliation;

import com.alligator.market.domain.provider.profile.model.Profile;
import com.alligator.market.domain.provider.profile.model.ProfileStatus;
import com.alligator.market.domain.provider.profile.repository.ProfileRepository;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Доменный сервис сопоставления профилей провайдеров.
 */
public final class ProfileReconciler {

    private final ProfileContextScanner contextScanner;
    private final ProfileRepository repository;

    // Конструктор
    public ProfileReconciler(ProfileContextScanner contextScanner,
                             ProfileRepository repository) {
        this.contextScanner = contextScanner;
        this.repository = repository;
    }

    /**
     * Сравнить профили в контексте и репозитории.
     *
     * @return {@link ProfileDiff}
     */
    public ProfileDiff compareContextAndRepository() {

        ProfileDiff diff = new ProfileDiff();

        Map<String, Profile> contextByCode = contextScanner.getProfiles().stream()
                .collect(Collectors.toMap(Profile::providerCode, Function.identity()));

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

