package com.alligator.market.domain.provider.service;

import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.provider.contract.InstrumentHandler;
import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.provider.exception.ProviderHandlerMismatchException;
import com.alligator.market.domain.provider.exception.ProviderHandlersInvalidException;
import com.alligator.market.domain.provider.exception.ProviderInstrumentHandlerDuplicateException;
import com.alligator.market.domain.provider.profile.exeption.ContextProfileDuplicateException;
import com.alligator.market.domain.provider.profile.model.Profile;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

/**
 * Сервис содержит методы для обслуживания провайдеров рыночных данных {@link MarketDataProvider}.
 */
public class ProviderCare {

    /**
     * Проверяет корректность набора обработчиков указанного провайдера.
     *
     * @param provider провайдер для проверки
     * @throws ProviderHandlersInvalidException              некорректный набор обработчиков
     * @throws ProviderHandlerMismatchException              обработчик относится к другому провайдеру
     * @throws ProviderInstrumentHandlerDuplicateException   дублирование обработчика по типу инструмента
     */
    public void validateHandlers(MarketDataProvider provider) {
        // 1) Проверяем, что список обработчиков не пустой и не содержит null элементы
        Set<InstrumentHandler> handlers = provider.getHandlers();
        if (handlers.isEmpty() || handlers.stream().anyMatch(Objects::isNull)) {
            throw new ProviderHandlersInvalidException();
        }

        // 2) Проверяем, что все обработчики соответствуют данному провайдеру
        String codeFromProfile = provider.getProfile().providerCode();
        Set<InstrumentType> instrumentTypes = new HashSet<>();
        for (InstrumentHandler handler : handlers) {
            String codeFromHandler = handler.getProviderCode();
            if (!codeFromProfile.equals(codeFromHandler)) {
                throw new ProviderHandlerMismatchException(codeFromProfile, codeFromHandler);
            }

            // 3) Проверяем, что тип инструмента уникален
            InstrumentType instrumentType = handler.getSupportedInstrumentType();
            if (!instrumentTypes.add(instrumentType)) {
                throw new ProviderInstrumentHandlerDuplicateException(instrumentType);
            }
        }
    }

    /** Проверяет уникальность профилей по кодам и именам. */
    public void validateNoDuplicates(List<Profile> profiles) {
        if (profiles == null || profiles.size() < 2) return; // Нечего проверять

        checkForDuplicateByParam(profiles, Profile::providerCode, "providerCode");
        checkForDuplicateByParam(profiles, Profile::displayName, "displayName");
    }

    /**
     * Метод проверки профилей на дублирование по значению заданного параметра.
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

