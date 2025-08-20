package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.provider.exception.InstrumentNotSupportedException;
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
    ProviderProfile profile();

    /** Возвращает набор обработчиков (handlers) данного провайдера. */
    Set<InstrumentHandler> handlers();

    /** Возвращает набор поддерживаемых типов инструментов (извлекаются из обработчиков). */
    default Set<InstrumentType> supportedInstrumentTypes() {
        return handlers().stream()
                .map(InstrumentHandler::supportedInstrument)
                .collect(java.util.stream.Collectors.toUnmodifiableSet());
    }

    /**
     * Находит handler для указанного типа инструмента.
     *
     * @return подходящий handler или null, если не найден
     */
    default InstrumentHandler findHandler(InstrumentType type) {
        for (InstrumentHandler h : handlers()) {
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
    default Flux<QuoteTick> quote(Instrument instrument) {
        InstrumentType type = instrument.type();
        InstrumentHandler handler = findHandler(type);
        if (handler == null) {
            return Flux.error(new InstrumentNotSupportedException(type, profile().providerCode()));
        }
        return handler.instrumentQuote(instrument);
    }

    /**
     * Вызывает проверки обработчиков провайдера.
     *
     * @throws IllegalStateException при нарушении инвариантов
     */
    default void validateHandlers() {
        // 1) Проверяем, что список обработчиков не пустой и не содержит null элементы
        if (handlers().isEmpty() || handlers().stream().anyMatch(Objects::isNull)) {
            throw new IllegalStateException("Handlers list must be non-empty and contain no null elements");
        }
        // 2) Проверяем, что все обработчики соответствуют данному провайдеру
        String codeFromProfile = profile().providerCode();
        Set<InstrumentType> instrumentTypes = new HashSet<>();
        for (InstrumentHandler handler : handlers()) {
            String codeFromHandler = handler.providerCode();
            if (!codeFromProfile.equals(codeFromHandler)) {
                throw new IllegalStateException(
                        "Handlers list of provider with code " + profile().providerCode() + " has at least one " +
                                "handler for another provider with code " + codeFromHandler
                );
            }
            // 3) Проверяем, что тип инструмента уникален
            InstrumentType instrumentType = handler.supportedInstrument();
            if (!instrumentTypes.add(instrumentType)) {
                throw new IllegalStateException("Duplicate handler for instrument type: " + instrumentType);
            }
        }
    }
}
