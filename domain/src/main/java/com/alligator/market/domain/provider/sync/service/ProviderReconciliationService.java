package com.alligator.market.domain.provider.sync.service;

import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.provider.profile.model.ProviderProfile;
import com.alligator.market.domain.provider.profile.contract.ProviderProfileStorage;
import com.alligator.market.domain.provider.sync.contract.ProviderContextScanner;
import com.alligator.market.domain.provider.sync.model.ProfileContextDiff;
import com.alligator.market.domain.provider.model.ProviderProfileStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Сервис, реализующий доменную логику обработки провайдеров рыночных данных.
 */
public class ProviderReconciliationService {

    private final ProviderContextScanner contextScanner;
    private final ProviderProfileStorage profileStorage;

    public ProviderReconciliationService(ProviderContextScanner contextScanner,
                                         ProviderProfileStorage profileStorage) {
        this.contextScanner = contextScanner;
        this.profileStorage = profileStorage;
    }

    // метод который извлекает модели MarketDataProvider в список, далее

    /**
     * Сравнить профили в хранилище данных и в контексте приложения.
     *
     * @return {@link ProfileContextDiff}
     */
    public ProfileContextDiff compare() {

        // Список профилей из контекста
        List<MarketDataProvider> contextProfiles = contextScanner.getProviders();
        // Активные профили из БД вместе с PK
        Map<Long, ProviderProfile> dbActiveProfiles = profileStorage.findAllActive();

        ProfileContextDiff diff = new ProfileContextDiff();

        // Задаем остаточный список, из которого при выполнении цикла будут удаляться найденные совпадения
        List<ProviderProfile> remainingContextProfiles =
                new ArrayList<>(contextProfiles); // До начала цикла копируем исходный список профилей из контекста

        // Перебираем активные профили из БД
        for (Map.Entry<Long, ProviderProfile> entry : dbActiveProfiles.entrySet()) {

            // entry — это текущая пара <PK, профиль из БД>
            Long id = entry.getKey();
            ProviderProfile dbProfile = entry.getValue();

            // Переменная, предназначенная для профиля провайдера, совпавшего по коду
            ProviderProfile contextMatch = null;

            // Перебираем профили из остаточного списка
            for (ProviderProfile p : remainingContextProfiles) {
                if (p.providerCode().equals(dbProfile.providerCode())) {
                    // Если найдено совпадение с текущим профилем из БД, помещаем этот профиль в "contextMatch"
                    contextMatch = p;
                    break;
                }
            }

            // 1) Если ни один профиль из контекста не совпал с текущим профилем из БД
            if (contextMatch == null) {
                diff.putToMissingList(id); // Помещаем текущий профиль БД в список для MISSING и
                continue;                  // переходим к следующему активному профилю из БД
            }

            // 2) Если полное совпадение по всем полям значит профиль актуален
            if (contextMatch.equals(dbProfile)) {
                remainingContextProfiles.remove(contextMatch); // Исключаем из остаточного списка и
                continue;                                      // переходим к следующему активному профилю из БД
            }

            // 3) Если совпадение есть, но не по всем полям профиля
            diff.putToReplaceList(id);       // Помещаем текущий профиль БД в список для замены (REPLACED)
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
