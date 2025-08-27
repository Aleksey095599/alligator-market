package com.alligator.market.domain.provider.reconciliation;

import com.alligator.market.domain.common.exception.DuplicateEntityException;
import com.alligator.market.domain.provider.profile.model.Profile;

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
    public static void validateNoDuplicates(List<Profile> profiles) {

        if (profiles == null || profiles.size() < 2) return; // Нечего проверять

        checkForDuplicateByParam(profiles, Profile::providerCode, "providerCode");
        checkForDuplicateByParam(profiles, Profile::displayName, "displayName");
    }

    /**
     * Метод проверки профилей на дублирование по значению заданного параметра.
     * Используется в методе {@link #validateNoDuplicates(List)}.
     *
     * @param profileList         список профилей для проверки
     * @param paramValueExtractor функция извлекает значение параметра
     * @param paramToCheck        параметр, по которому проверяем
     *
     * @throws DuplicateEntityException при обнаружении дублирования
     */
    private static void checkForDuplicateByParam(List<Profile> profileList,
                                                 Function<Profile, String> paramValueExtractor,
                                                 String paramToCheck) {

        // Коллекция типа HashSet для хранения только уникальных значений
        Set<String> paramValues = new HashSet<>();

        // Перебираем все профили из списка
        for (Profile p : profileList) {

            // Извлекаем значение параметра из данного профиля
            String value = paramValueExtractor.apply(p);

            // Приводим к нижнему регистру
            if (value != null) {
                value = value.trim().toLowerCase(Locale.ROOT);
            }

            // Если при попытке добавить значение параметра в коллекцию ошибка => нарушение уникальности
            if (!paramValues.add(value)) {
                throw new DuplicateEntityException("Provider profile", paramToCheck, String.valueOf(value));
            }
        }
    }
}
