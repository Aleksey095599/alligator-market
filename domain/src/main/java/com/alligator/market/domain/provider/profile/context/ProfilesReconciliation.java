package com.alligator.market.domain.provider.profile.context;

import com.alligator.market.domain.provider.profile.model.ProviderProfile;
import com.alligator.market.domain.provider.profile.catalog.ProviderProfileStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Сервис, реализующий доменную логику сопоставления профилей провайдеров рыночных данных,
 * извлеченных из контекста приложения и из хранилища данных.
 */
public class ProfilesReconciliation {

    private final ProfileContextScanner contextScanner;
    private final ProviderProfileStorage profileStorage;

    public ProfilesReconciliation(ProfileContextScanner contextScanner,
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
        List<ProviderProfile> restContextProfiles =
                new ArrayList<>(contextProfiles); // До начала цикла совпадает с полным списком

        // Перебираем все профили из БД
        for (Map.Entry<Long, ProviderProfile> entry_i : dbActiveProfiles.entrySet()) {

            // entry_i это пара <PK, i-ый профиль>
            Long id = entry_i.getKey();
            ProviderProfile dbProfile = entry_i.getValue();

            // Переменная, предназначенная для хранения профиля провайдера, совпавшего по коду
            ProviderProfile contextMatch = null;

            // Перебираем профили из списка "restContextProfiles"
            for (ProviderProfile p : restContextProfiles) {
                if (p.providerCode().equals(dbProfile.providerCode())) {
                    // Если найдено совпадение с i-ым профилем из БД
                    contextMatch = p; // помещаем этот профиль в "contextMatch"
                    break;
                }
            }

            // 1) Если ни один профиль из контекста не совпал с i-ым профилем из БД
            if (contextMatch == null) {
                diff.putToMissingList(id); // Значит профиль в БД не актуален (MISSING)
                continue; // переходим к i+1 профилю из БД
            }

            // 2) Если полное совпадение по всем полям значит профиль актуален
            if (contextMatch.equals(dbProfile)) {
                // Исключаем из "restContextProfiles", в БД i-ый профиль останется без изменений
                restContextProfiles.remove(contextMatch);
                continue; // переходим к i+1 профилю из БД
            }

            // 3) Если совпадение есть, но не по всем полям профиля
            diff.putToReplaceList(id); // Помещаем i-ый профиль БД в список для замены (REPLACED)
            diff.putToAddList(contextMatch); // Профиль их контекста добавляем в список для добавления как ACTIVE

            // Удаляем из "restContextProfiles" обработанный профиль "contextMatch"
            restContextProfiles.remove(contextMatch);
        }

        // Оставшиеся профили не нашли совпадений с БД, значит их добавляем в список для добавления как ACTIVE
        restContextProfiles.forEach(diff::putToAddList);
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
