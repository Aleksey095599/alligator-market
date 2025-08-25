package com.alligator.market.domain.provider.service;

import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.provider.handler.contract.InstrumentHandler;
import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.provider.exception.ProviderHandlerMismatchException;
import com.alligator.market.domain.provider.exception.ProviderHandlersInvalidException;
import com.alligator.market.domain.provider.exception.ProviderInstrumentHandlerDuplicateException;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Сервис содержит методы для обслуживания провайдеров рыночных данных {@link MarketDataProvider}.
 */
public class ProviderService {

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
}

