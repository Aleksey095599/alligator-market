package com.alligator.market.domain.provider.reconciliation;

import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.profile.repository.ProfileRepository;

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

        Map<String, ProviderDescriptor> contextByCode = providerContextScanner.getProfiles();

        repository.findAllActive().forEach((id, repoProfile) -> {
            ProviderDescriptor contextProviderDescriptor = contextByCode.remove(repoProfile.providerCode());
            if (contextProviderDescriptor == null) {
                diff.putToMissingList(id);
            } else if (!contextProviderDescriptor.equals(repoProfile)) {
                diff.putToReplaceList(id);
                diff.putToAddList(contextProviderDescriptor);
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

