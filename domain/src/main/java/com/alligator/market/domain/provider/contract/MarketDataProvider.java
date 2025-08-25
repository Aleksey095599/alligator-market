package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.provider.exception.InstrumentNotSupportedException;
import com.alligator.market.domain.provider.handler.contract.InstrumentHandler;
import com.alligator.market.domain.provider.profile.model.ProviderProfile;
import com.alligator.market.domain.quote.QuoteTick;
import com.alligator.market.domain.instrument.type.InstrumentType;
import java.util.Set;

import reactor.core.publisher.Flux;

/**
 * Контракт провайдера рыночных данных.
 */
public interface MarketDataProvider {

    /** Возвращает профиль провайдера {@link ProviderProfile}. */
    ProviderProfile getProfile();

    /** Возвращает набор обработчиков {@link InstrumentHandler}. */
    Set<InstrumentHandler> getHandlers();

    /**
     * Возвращает котировку.
     *
     * @throws InstrumentNotSupportedException если подходящий обработчик инструмента не найден
     */
    default Flux<QuoteTick> getQuote(Instrument instrument) {
        InstrumentType type = instrument.getType();
        InstrumentHandler handler = findHandlerForInstrument(type);
        if (handler == null) {
            return Flux.error(new InstrumentNotSupportedException(type, getProfile().providerCode()));
        }
        return handler.getInstrumentQuote(instrument);
    }

    /**
     * Находит обработчик для указанного типа инструмента.
     *
     * @return подходящий обработчик или null
     */
    default InstrumentHandler findHandlerForInstrument(InstrumentType type) {
        for (InstrumentHandler h : getHandlers()) {
            if (h.getSupportedInstrumentType() == type) {
                return h;
            }
        }
        return null;
    }
}
