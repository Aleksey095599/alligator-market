package com.alligator.market.domain.provider.sync.service;

import com.alligator.market.domain.provider.profile.model.ProviderProfile;
import com.alligator.market.domain.provider.profile.contract.ProviderProfileStorage;
import com.alligator.market.domain.provider.sync.contract.ProfileContextScanner;
import com.alligator.market.domain.provider.sync.model.ProfileContextDiff;
import com.alligator.market.domain.provider.model.ProviderProfileStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Сервис, реализующий доменную логику сопоставления профилей провайдеров рыночных данных,
 * извлеченных из контекста приложения и из хранилища данных.
 */
public class ProfilesReconciliationService {

    private final ProfileContextScanner contextScanner;
    private final ProviderProfileStorage profileStorage;

    public ProfilesReconciliationService(ProfileContextScanner contextScanner,
                                         ProviderProfileStorage profileStorage) {
        this.contextScanner = contextScanner;
        this.profileStorage = profileStorage;
    }

    /**
     * Сравнить профили в хранилище данных и в контексте приложения.
     *
     * @return {@link ProfileContextDiff}
     */
    public ProfileContextDiff compare() {

        // Профили из контекста
        List<ProviderProfile> contextProfiles = contextScanner.getProviderProfiles();
        // Профили из БД в виде набора <PK, активный профиль>
        Map<Long, ProviderProfile> dbActiveProfiles = profileStorage.findAllActive();

        ProfileContextDiff diff = new ProfileContextDiff();

        // Копия списка профилей из контекста, из которого удаляются найденные совпадения
        List<ProviderProfile> remainingContextProfiles =
                new ArrayList<>(contextProfiles); // До начала цикла совпадает с полным списком

        // Перебираем все профили из БД
        for (Map.Entry<Long, ProviderProfile> entry : dbActiveProfiles.entrySet()) {

            // entry — это пара <PK, профиль из БД>
            Long id = entry.getKey();
            ProviderProfile dbProfile = entry.getValue();

            // Переменная, предназначенная для хранения профиля провайдера, совпавшего по коду
            ProviderProfile contextMatch = null;

            // Перебираем профили из списка "remainingContextProfiles"
            for (ProviderProfile p : remainingContextProfiles) {
                if (p.providerCode().equals(dbProfile.providerCode())) {
                    // Если найдено совпадение с текущим профилем из БД
                    contextMatch = p; // помещаем этот профиль в "contextMatch"
                    break;
                }
            }

            // 1) Если ни один профиль из контекста не совпал с текущим профилем из БД
            if (contextMatch == null) {
                diff.putToMissingList(id); // Значит профиль в БД не актуален (MISSING)
                continue; // переходим к следующему профилю из БД
            }

            // 2) Если полное совпадение по всем полям значит профиль актуален
            if (contextMatch.equals(dbProfile)) {
                // Исключаем из "remainingContextProfiles", в БД текущий профиль останется без изменений
                remainingContextProfiles.remove(contextMatch);
                continue; // переходим к следующему профилю из БД
            }

            // 3) Если совпадение есть, но не по всем полям профиля
            diff.putToReplaceList(id); // Помещаем текущий профиль БД в список для замены (REPLACED)
            diff.putToAddList(contextMatch); // Профиль из контекста добавляем в список для добавления как ACTIVE

            // Удаляем из "remainingContextProfiles" обработанный профиль "contextMatch"
            remainingContextProfiles.remove(contextMatch);
        }

        // Оставшиеся профили не нашли совпадений с БД, значит их добавляем в список для добавления как ACTIVE
        remainingContextProfiles.forEach(diff::putToAddList);
        return diff;
    }

    /**
     * Применить {@link ProfileContextDiff} к хранилищу данных для приведения к актуальному состоянию.
     */
    public void applyContextDiffToStorage(ProfileContextDiff diff) {
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
