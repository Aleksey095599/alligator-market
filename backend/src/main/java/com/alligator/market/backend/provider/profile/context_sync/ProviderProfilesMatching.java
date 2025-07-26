package com.alligator.market.backend.provider.profile.context_sync;

import com.alligator.market.backend.provider.profile.service.ProviderProfileService;
import com.alligator.market.backend.provider.profile.exception.DuplicateProviderProfileException;
import com.alligator.market.domain.provider.profile.ProviderProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Компонент сравнивает профили провайдеров, извлеченных из контекста Spring и извлеченных из базы данных,
 * возвращает наборы профилей в виде {@link CompareResult} для дальнейших действий в целях синхронизации.
 */
@Component
@RequiredArgsConstructor
public class ProviderProfilesMatching {

    private final ProviderContextScanner contextScanner;
    private final ProviderProfileService profileService;

    public CompareResult compare() {

        // Извлекаем профили из контекста
        List<ProviderProfile> contextProfiles = contextScanner.getProviderProfiles();

        // Проверяем, что среди профилей из контекста нет профилей с совпадающими providerCode или displayName
        Set<String> codes = new HashSet<>();
        Set<String> names = new HashSet<>();
        for (ProviderProfile profile : contextProfiles) {
            if (!codes.add(profile.providerCode())) {
                throw new DuplicateProviderProfileException("providerCode", profile.providerCode());
            }
            if (!names.add(profile.displayName())) {
                throw new DuplicateProviderProfileException("displayName", profile.displayName());
            }
        }

        // Извлекаем активные профили из таблицы вместе с PK
        Map<ProviderProfile, Long> dbActiveProfiles = profileService.findAllActive();

        //===============================
        // Процесс сопоставления профилей
        //===============================

        // Контейнеры для результата
        List<ProviderProfile> addNew = new ArrayList<>();
        Map<ProviderProfile, Long> toReplaced = new LinkedHashMap<>();
        Map<ProviderProfile, Long> toMissing = new LinkedHashMap<>();

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
                toMissing.put(dbProfile, id);
                continue;
            }

            if (contextMatch.equals(dbProfile)) {
                // Полное совпадение
                restContextProfiles.remove(contextMatch);
                continue;
            }

            // providerCode совпадает, но есть отличия
            toReplaced.put(dbProfile, id);
            addNew.add(contextMatch);
            restContextProfiles.remove(contextMatch);
        }

        // Оставшиеся в контексте профили новые
        addNew.addAll(restContextProfiles);

        return new CompareResult(addNew, toReplaced, toMissing);
    }
}
