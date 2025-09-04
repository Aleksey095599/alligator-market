package com.alligator.market.domain.provider.handler.service;

import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.provider.exception.ProviderHandlerMismatchException;
import com.alligator.market.domain.provider.exception.ProviderHandlersInvalidException;
import com.alligator.market.domain.provider.exception.ProviderInstrumentHandlerDuplicateException;
import com.alligator.market.domain.provider.handler.contract.InstrumentHandler;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class HandlerValidator {

    /**
     * Проверяет корректность набора обработчиков указанного провайдера.
     *
     * @param provider провайдер для проверки
     *
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

    /**
     * Проверяет корректность обработчиков из контекста.
     */
    public void validateHandlers(Map<String, Map<InstrumentType, InstrumentHandler>> contextHandlers) {
        if (contextHandlers == null || contextHandlers.isEmpty()) return; // Нечего проверять

        for (Map.Entry<String, Map<InstrumentType, InstrumentHandler>> entry : contextHandlers.entrySet()) {
            String providerCode = entry.getKey();
            Map<InstrumentType, InstrumentHandler> handlers = entry.getValue();

            // 1) Проверяем, что список обработчиков не пустой и не содержит null элементы
            if (handlers == null || handlers.isEmpty() || handlers.values().stream().anyMatch(Objects::isNull)) {
                throw new ProviderHandlersInvalidException();
            }

            for (InstrumentHandler handler : handlers.values()) {
                String codeFromHandler = handler.getProviderCode();
                if (!providerCode.equals(codeFromHandler)) {
                    throw new ProviderHandlerMismatchException(providerCode, codeFromHandler);
                }
            }
        }
    }
}
