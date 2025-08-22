package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.provider.exception.InstrumentNotSupportedException;
import com.alligator.market.domain.provider.exception.ProviderHandlersInvalidException;
import com.alligator.market.domain.provider.exception.ProviderHandlerMismatchException;
import com.alligator.market.domain.provider.exception.ProviderInstrumentHandlerDuplicateException;
import com.alligator.market.domain.provider.profile.model.ProviderProfile;
import com.alligator.market.domain.quote.QuoteTick;
import com.alligator.market.domain.instrument.contract.InstrumentType;

import java.util.Objects;
import java.util.Set;
import java.util.HashSet;

import reactor.core.publisher.Flux;

/**
 * Контракт провайдера рыночных данных.
 */
public interface MarketDataProvider {

    /** Возвращает профиль провайдера. */
    ProviderProfile getProfile();

    /** Возвращает код провайдера. */
    default String getProviderCode() {
        return getProfile().providerCode();
    }

    /** Возвращает набор обработчиков (handlers) данного провайдера. */
    Set<InstrumentHandler> getHandlers();

    /** Возвращает набор поддерживаемых типов инструментов (извлекаются из обработчиков). */
    default Set<InstrumentType> getSupportedInstrumentTypes() {
        return getHandlers().stream()
                .map(InstrumentHandler::supportedInstrument)
                .collect(java.util.stream.Collectors.toUnmodifiableSet());
    }

    /**
     * Находит обработчик для указанного типа инструмента.
     *
     * @return подходящий обработчик или null, если не найден
     */
    default InstrumentHandler findHandlerForInstrument(InstrumentType type) {
        for (InstrumentHandler h : getHandlers()) {
            if (h.supportedInstrument() == type) {
                return h;
            }
        }
        return null;
    }

    /**
     * Возвращает котировку.
     *
     * @throws InstrumentNotSupportedException если подходящий обработчик инструмента не найден
     */
    default Flux<QuoteTick> getQuote(Instrument instrument) {
        InstrumentType type = instrument.type();
        InstrumentHandler handler = findHandlerForInstrument(type);
        if (handler == null) {
            return Flux.error(new InstrumentNotSupportedException(type, getProfile().providerCode()));
        }
        return handler.instrumentQuote(instrument);
    }

    /**
     * Вызывает проверки обработчиков провайдера.
     *
     * @throws ProviderHandlersInvalidException              некорректный набор обработчиков
     * @throws ProviderHandlerMismatchException              обработчик относится к другому провайдеру
     * @throws ProviderInstrumentHandlerDuplicateException   дублирование обработчика по типу инструмента
     */
    default void validateHandlers() {
        // 1) Проверяем, что список обработчиков не пустой и не содержит null элементы
        if (getHandlers().isEmpty() || getHandlers().stream().anyMatch(Objects::isNull)) {
            throw new ProviderHandlersInvalidException();
        }
        // 2) Проверяем, что все обработчики соответствуют данному провайдеру
        String codeFromProfile = getProfile().providerCode();
        Set<InstrumentType> instrumentTypes = new HashSet<>();
        for (InstrumentHandler handler : getHandlers()) {
            String codeFromHandler = handler.providerCode();
            if (!codeFromProfile.equals(codeFromHandler)) {
                throw new ProviderHandlerMismatchException(codeFromProfile, codeFromHandler);
            }
            // 3) Проверяем, что тип инструмента уникален для каждого обработчика
            InstrumentType instrumentType = handler.supportedInstrument();
            if (!instrumentTypes.add(instrumentType)) {
                throw new ProviderInstrumentHandlerDuplicateException(instrumentType);
            }
        }
    }
}
