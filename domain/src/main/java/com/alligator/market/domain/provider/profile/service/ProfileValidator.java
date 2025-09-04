package com.alligator.market.domain.provider.profile.service;

import com.alligator.market.domain.provider.profile.exeption.ContextProfileDuplicateException;
import com.alligator.market.domain.provider.profile.model.Profile;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Function;
import java.util.Map;

public class ProfileValidator {

    /** Проверяет уникальность профилей по кодам и именам. */
    public void validateNoDuplicates(List<Profile> profiles) {
        if (profiles == null || profiles.size() < 2) return; // Нечего проверять

        checkForDuplicateByParam(profiles, Profile::providerCode, "providerCode");
        checkForDuplicateByParam(profiles, Profile::displayName, "displayName");
    }

    /** Проверяет уникальность профилей из контекста. */
    public void validateNoDuplicates(Map<String, Profile> contextProfiles) {
        if (contextProfiles == null || contextProfiles.size() < 2) return; // Нечего проверять

        Set<String> codes = new HashSet<>();
        Set<String> names = new HashSet<>();
        for (Profile p : contextProfiles.values()) {
            String code = p.providerCode();
            if (code != null) {
                code = code.trim().toLowerCase(Locale.ROOT);
            }
            if (!codes.add(code)) {
                throw new ContextProfileDuplicateException("providerCode", String.valueOf(code));
            }

            String name = p.displayName();
            if (name != null) {
                name = name.trim().toLowerCase(Locale.ROOT);
            }
            if (!names.add(name)) {
                throw new ContextProfileDuplicateException("displayName", String.valueOf(name));
            }
        }
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
