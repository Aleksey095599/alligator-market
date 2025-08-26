package com.alligator.market.domain.provider.reconciliation;

import com.alligator.market.domain.provider.profile.model.Profile;
import com.alligator.market.domain.provider.profile.model.ProfileStatus;
import com.alligator.market.domain.provider.profile.repository.ProfileRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Сервис, реализующий логику сопоставления данных о профиле провайдера из репозитория и контекста приложения.
 */
public interface ProfileReconciler {

    private final ProfileContextScanner contextScanner;
    private final ProfileRepository repository;

    public ProfileReconciler(ProfileContextScanner contextScanner,
                             ProfileRepository repository) {
        this.contextScanner = contextScanner;
        this.repository = repository;
    }

    /**
     * Сравнить профили в репозитории и контексте приложения.
     *
     * @return {@link ProfileDiff}
     */
    public ProfileDiff compareContextAndRepository() {

        // Список профилей из контекста
        List<Profile> contextProfiles = contextScanner.getProfiles();
        // Актуальные профили из репозитория вместе с PK
        Map<Long, Profile> repoProfiles = repository.findAllActive();

        // Переменная для заполнения результатами сравнения
        ProfileDiff diff = new ProfileDiff();

        // Задаем остаточный список, из которого при выполнении цикла будут удаляться найденные совпадения
        List<Profile> remainingContextProfiles =
                new ArrayList<>(contextProfiles); // До начала цикла копируем исходный список профилей из контекста

        // Перебираем активные профили из репозитория
        for (Map.Entry<Long, Profile> entry : repoProfiles.entrySet()) {

            // entry — это текущая пара <PK, профиль из репозитория>
            Long currentId = entry.getKey();
            Profile currentRepoProfile = entry.getValue();

            // Переменная, предназначенная для профиля провайдера, совпавшего по коду
            Profile contextMatch = null;

            // Перебираем профили из остаточного списка контекста
            for (Profile p : remainingContextProfiles) {
                if (p.providerCode().equals(currentRepoProfile.providerCode())) {
                    // Если найдено совпадение с текущим профилем из репозитория, помещаем этот профиль в "contextMatch"
                    contextMatch = p;
                    break;
                }
            }

            // 1) Если ни один профиль из контекста не совпал с текущим профилем из репозитория
            if (contextMatch == null) {
                diff.putToMissingList(currentId); // Помещаем текущий профиль из репозитория в список для MISSING
                continue;
            }

            // 2) Если полное совпадение по всем полям значит профиль актуален
            if (contextMatch.equals(currentRepoProfile)) {
                remainingContextProfiles.remove(contextMatch); // Исключаем из остаточного списка
                continue;
            }

            // 3) Если совпадение есть, но не по всем полям профиля
            diff.putToReplaceList(currentId); // Помещаем текущий профиль БД в список для замены (REPLACED)
            diff.putToAddList(contextMatch); // Профиль из контекста добавляем в список для добавления как ACTIVE

            // Исключаем из остаточного списка
            remainingContextProfiles.remove(contextMatch);
        }

        // Оставшиеся профили не нашли совпадений с репозиторием, значит в список добавить как ACTIVE
        remainingContextProfiles.forEach(diff::putToAddList);
        return diff;
    }

    /**
     * Применить {@link ProfileDiff} к репозиторию для приведения к актуальному состоянию.
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
