package com.alligator.market.domain.provider.handler.service;

import com.alligator.market.domain.instrument.type.InstrumentType;
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
     * Проверяет корректность обработчиков из контекста.
     *
     * @param contextHandlers карта обработчиков <providerCode, <InstrumentType, InstrumentHandler>>
     *
     * @throws ProviderHandlersInvalidException              некорректный набор обработчиков
     * @throws ProviderHandlerMismatchException              обработчик относится к другому провайдеру
     * @throws ProviderInstrumentHandlerDuplicateException   дублирование обработчика по типу инструмента
     */
    public void validateHandlers(Map<String, Map<InstrumentType, InstrumentHandler>> contextHandlers) {
        // Карта не должна быть пустой или null
        if (contextHandlers == null || contextHandlers.isEmpty()) {
            throw new ProviderHandlersInvalidException(); // TODO: создать и заменить на ошибку типа нет ни одного обработчика инструментов
        }

        for (Map.Entry<String, Map<InstrumentType, InstrumentHandler>> entry : contextHandlers.entrySet()) {
            String providerCode = entry.getKey();
            Map<InstrumentType, InstrumentHandler> handlersByInstrumentType = entry.getValue();

            // 1) Проверяем, что список обработчиков не пустой и не содержит null элементы
            if (handlersByInstrumentType == null || handlersByInstrumentType.isEmpty() || handlersByInstrumentType.values().stream().anyMatch(Objects::isNull)) {
                throw new ProviderHandlersInvalidException();
            }

            Set<InstrumentType> instrumentTypes = new HashSet<>();

            for (Map.Entry<InstrumentType, InstrumentHandler> handlerEntry : handlersByInstrumentType.entrySet()) {
                InstrumentType instrumentType = handlerEntry.getKey();
                InstrumentHandler handler = handlerEntry.getValue();

                // 2) Проверяем соответствие провайдеру
                String codeFromHandler = handler.getProviderCode();
                if (!providerCode.equals(codeFromHandler)) {
                    throw new ProviderHandlerMismatchException(providerCode, codeFromHandler);
                }

                // 3) Проверяем уникальность и корректность типа инструмента
                InstrumentType supportedType = handler.getSupportedInstrumentType();
                if (supportedType != instrumentType) {
                    throw new ProviderHandlersInvalidException();
                }
                if (!instrumentTypes.add(supportedType)) {
                    throw new ProviderInstrumentHandlerDuplicateException(supportedType);
                }
            }
        }
    }
}
