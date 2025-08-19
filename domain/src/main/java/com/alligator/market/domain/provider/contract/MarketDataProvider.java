package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.provider.exeption.InstrumentNotSupportedException;
import com.alligator.market.domain.provider.profile.model.ProviderProfile;
import com.alligator.market.domain.quote.QuoteTick;
import com.alligator.market.domain.instrument.contract.InstrumentType;

import java.util.Set;

import reactor.core.publisher.Flux;

/**
 * Контракт провайдера рыночных данных.
 */
public interface MarketDataProvider {

    /** Возвращает профиль провайдера. */
    ProviderProfile profile();

    /** Возвращает набор обработчиков данного провайдера. */
    Set<InstrumentHandler> handlers();

    /** Возвращает набор поддерживаемых типов инструментов. */
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
        // → линейный поиск (O(n)); см. примечание про возможную индексацию в абстрактном базовом классе
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
     * Проверяет, что все обработчики принадлежат этому провайдеру
     * и нет дублирования по типам инструментов.
     *
     * @throws IllegalStateException при нарушении инвариантов
     */
    default void validateHandlers() {
        String codeFromProfile = profile().providerCode();
        for (InstrumentHandler handler : handlers()) {
            if (handler == null) {
                throw new IllegalStateException("Handler must not be null");
            }
            String codeFromHandler = handler.providerCode();
            if (!codeFromProfile.equals(codeFromHandler)) {
                throw new IllegalStateException(
                        "Provider/handler mismatch: expected '" + codeFromProfile +
                                "', but handler has '" + codeFromHandler + "'"
                );
            }
        }
    }
}
