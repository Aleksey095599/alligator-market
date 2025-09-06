package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.base.Instrument;
import com.alligator.market.domain.provider.exception.InstrumentNotSupportedException;
import com.alligator.market.domain.provider.handler.contract.InstrumentHandler;
import com.alligator.market.domain.provider.profile.model.Profile;
import com.alligator.market.domain.quote.QuoteTick;
import com.alligator.market.domain.instrument.type.InstrumentType;
import java.util.Set;

import org.reactivestreams.Publisher;

/**
 * Контракт провайдера рыночных данных.
 */
public interface MarketDataProvider {

    /** Возвращает профиль провайдера {@link Profile}. */
    Profile getProfile();

    /** Возвращает набор обработчиков {@link InstrumentHandler}. */
    Set<InstrumentHandler> getHandlers();

    /**
     * Возвращает котировку.
     *
     * @throws InstrumentNotSupportedException если подходящий обработчик инструмента не найден
     */
    Publisher<QuoteTick> getQuote(Instrument instrument) throws InstrumentNotSupportedException;

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
