package com.alligator.market.backend.provider.sync.scanner;

import com.alligator.market.domain.provider.sync.exeption.ContextProfileDuplicateException;
import com.alligator.market.domain.provider.model.profile.ProviderProfile;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Function;

/**
 * Вспомогательный класс, содержащий методы для проверки профилей провайдеров рыночных данных,
 * выгруженных из контекста приложения.
 */
public final class ProfileContextCheck {

    private ProfileContextCheck() {
    }

    /** Проверяет уникальность профилей по кодам и именам. */
    public static void validateNoDuplicates(List<ProviderProfile> profiles) {

        if (profiles == null || profiles.size() < 2) return; // Нечего проверять

        checkForDuplicateByParam(profiles, ProviderProfile::providerCode, "providerCode");
        checkForDuplicateByParam(profiles, ProviderProfile::displayName, "displayName");
    }

    /**
     * Проверяет список профилей на дублирование по значению заданного параметра.
     *
     * @param profileList         список профилей для проверки
     * @param paramValueExtractor функция извлекает значение параметра
     * @param paramToCheck        параметр, по которому проверяем
     *
     * @throws ContextProfileDuplicateException при обнаружении дублирования
     */
    private static void checkForDuplicateByParam(List<ProviderProfile> profileList,
                                                 Function<ProviderProfile, String> paramValueExtractor,
                                                 String paramToCheck) {

        // Коллекция типа HashSet для хранения только уникальных значений
        Set<String> paramValues = new HashSet<>();

        // Перебираем все профили из списка
        for (ProviderProfile p : profileList) {

            // Извлекаем значение параметра из данного профиля
            String value = paramValueExtractor.apply(p);

            // Приводим к нижнему регистру
            if (value != null) {
                value = value.trim().toLowerCase(Locale.ROOT);
            }

            // Если при попытке добавить значение параметра в коллекцию ошибка => нарушение уникальности
            if (!paramValues.add(value)) {
                throw new ContextProfileDuplicateException(paramToCheck, String.valueOf(value));
            }
        }
    }
}
