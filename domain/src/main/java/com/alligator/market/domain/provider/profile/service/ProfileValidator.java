package com.alligator.market.domain.provider.profile.service;

import com.alligator.market.domain.provider.profile.exeption.ContextProfileDuplicateException;
import com.alligator.market.domain.provider.profile.model.Profile;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Function;
import java.util.Map;
import java.util.stream.Collectors;

public class ProfileValidator {

    /** Проверяет уникальность профилей по кодам и именам. */
    public void validateNoDuplicates(List<Profile> profiles) {
        if (profiles == null || profiles.size() < 2) return; // Нечего проверять

        checkForDuplicateByParam(profiles, Profile::providerCode, "providerCode");
        checkForDuplicateByParam(profiles, Profile::displayName, "displayName");
    }

    /** Проверяет уникальность профилей из контекста. */
    public void validateNoDuplicates(Map<String, Map<String, Profile>> contextProfiles) {
        if (contextProfiles == null || contextProfiles.isEmpty()) return; // Нечего проверять

        List<Profile> profiles = contextProfiles.values().stream()
                .flatMap(m -> m.values().stream())
                .collect(Collectors.toList());

        validateNoDuplicates(profiles);
    }

    /**
     * Вспомогательный метод проверки профилей на дублирование по параметру.
     *
     * @param profileList список профилей для проверки
     * @param paramValueExtractor функция извлекает значение параметра
     * @param paramToCheck параметр, по которому проверяем
     *
     * @throws ContextProfileDuplicateException при обнаружении дублирования
     */
    private void checkForDuplicateByParam(List<Profile> profileList,
                                          Function<Profile, String> paramValueExtractor,
                                          String paramToCheck) {
        Set<String> paramValues = new HashSet<>();
        for (Profile p : profileList) {
            String value = paramValueExtractor.apply(p);
            if (value != null) {
                value = value.trim().toLowerCase(Locale.ROOT);
            }

            if (!paramValues.add(value)) {
                throw new ContextProfileDuplicateException(paramToCheck, String.valueOf(value));
            }
        }
    }
}
