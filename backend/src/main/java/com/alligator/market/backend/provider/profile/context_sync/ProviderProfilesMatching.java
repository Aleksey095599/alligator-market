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

        // Проверяем, что профили из контекста имеют разные providerCode и displayName
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

        // Перебираем профили из dbActiveProfiles. Если i-ый профиль из dbActiveProfiles полностью (по всем полям)
        // совпадает с некоторым j-ым профилем из contextProfiles значит в базе данных есть данный активный профиль - убираем j-ый профиль из contextProfiles.
        // Если вдруг i-ый профиль из dbActiveProfiles вообще не найден в contextProfiles значит добавляем этот профиль в
        // CompareResult в список changeStatusToMissing. Если i-ый профиль из dbActiveProfiles совпадает по полю providerCode некоторым j-ым профилем из contextProfiles,
        // однако по другим полям есть хотя бы одно отличие, значит i-ый профиль из dbActiveProfiles добавляем в список changeStatusToReplaced,
        // а j-ый профиль из contextProfiles добавляем в список addNewWithActiveStatus и убираем из списка contextProfiles. Если после того, как мы перебрали все профили из dbActiveProfiles
        // в списке contextProfiles остались профили - значит это новые профили их добавляем в addNewWithActiveStatus

    }
}
