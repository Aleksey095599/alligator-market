package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.provider.exception.InstrumentNotSupportedException;
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

}
