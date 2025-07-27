package com.alligator.market.backend.provider.profile.context_sync;

import com.alligator.market.backend.provider.profile.service.ProviderProfileService;
import com.alligator.market.domain.provider.profile.ProviderProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Компонент сравнивает профили провайдеров, извлеченных из контекста Spring и извлеченных из базы данных,
 * возвращает наборы профилей в виде {@link ContextDiff} для дальнейших действий в целях синхронизации.
 */
@Component
@RequiredArgsConstructor
public class ProviderProfilesMatching {

    private final ProviderContextScanner contextScanner;
    private final ProviderProfileService profileService;

    public ContextDiff compare() {

        // Извлекаем профили из контекста
        List<ProviderProfile> contextProfiles = contextScanner.getProviderProfiles();


        // Извлекаем активные профили из таблицы вместе с PK
        Map<ProviderProfile, Long> dbActiveProfiles = profileService.findAllActive();

        //===============================
        // Процесс сопоставления профилей
        //===============================

        // Контейнеры для результата
        ContextDiff diff = new ContextDiff();

        // Создаем копию списка профилей из контекста, чтобы удалять найденные
        List<ProviderProfile> restContextProfiles = new ArrayList<>(contextProfiles);

        // Перебираем профили из БД
        for (Map.Entry<ProviderProfile, Long> entry : dbActiveProfiles.entrySet()) {

            ProviderProfile dbProfile = entry.getKey();
            Long id = entry.getValue();

            // Ищем профиль с тем же providerCode в контексте
            ProviderProfile contextMatch = null;
            for (ProviderProfile p : restContextProfiles) {
                if (p.providerCode().equals(dbProfile.providerCode())) {
                    contextMatch = p;
                    break;
                }
            }

            if (contextMatch == null) {
                // Профиль в контексте не найден
                diff.putToMissingMap(dbProfile, id);
                continue;
            }

            if (contextMatch.equals(dbProfile)) {
                // Полное совпадение
                restContextProfiles.remove(contextMatch);
                continue;
            }

            // providerCode совпадает, но есть отличия
            diff.putToReplaceMap(dbProfile, id);
            diff.putAddList(contextMatch);
            restContextProfiles.remove(contextMatch);
        }

        // Оставшиеся в контексте профили новые
        restContextProfiles.forEach(diff::putAddList);

        return diff;
    }
}
