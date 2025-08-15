package com.alligator.market.domain.provider.context_sync;

import com.alligator.market.domain.provider.profile.model.ProviderProfile;
import com.alligator.market.domain.provider.profile.catalog.ProviderProfileStorage;
import com.alligator.market.domain.provider.profile.model.ProviderProfileStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Сервис, реализующий доменную логику сопоставления профилей провайдеров рыночных данных (далее - профили),
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
     * Сравнить профили в хранилище данных и в контексте приложения.
     *
     * @return {@link ContextDiff}
     */
    public ContextDiff compare() {

        // Профили из контекста
        List<ProviderProfile> contextProfiles = contextScanner.getProviderProfiles();
        // Профили из БД в виде набора <PK, активный профиль>
        Map<Long, ProviderProfile> dbActiveProfiles = profileStorage.findAllActive();

        ContextDiff diff = new ContextDiff();

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
                // Исключаем из "restContextProfiles",
                // в БД i-ый профиль останется без изменений
                restContextProfiles.remove(contextMatch);
                continue; // переходим к i+1 профилю из БД
            }

            // 3) Сюда приходим, если совпадение есть, но не полное
            diff.putToReplaceList(id); // Помещаем в список для замены (REPLACED)

            // 4) Сюда приходим если ???
            diff.putToAddList(contextMatch);

            // Удаляем из "restContextProfiles" обработанный профиль "contextMatch"
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
