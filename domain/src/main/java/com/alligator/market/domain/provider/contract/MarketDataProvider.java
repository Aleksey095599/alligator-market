package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.base.contract.Instrument;
import com.alligator.market.domain.provider.exception.InstrumentNotSupportedException;
import com.alligator.market.domain.provider.handler.contract.InstrumentHandler;
import com.alligator.market.domain.provider.profile.model.Profile;
import com.alligator.market.domain.quote.QuoteTick;
import org.reactivestreams.Publisher;

/**
 * Контракт провайдера рыночных данных.
 */
public interface MarketDataProvider {

    /** Профиль провайдера {@link Profile}. */
    Profile profile();

    /** Возвращает набор обработчиков. */


    /**
     * Возвращает обработчик для указанного инструмента.
     *
     * @throws InstrumentNotSupportedException если обработчик для инструмента не найден
     */
    InstrumentHandler<? extends MarketDataProvider, ? extends Instrument> getHandler(Instrument instrument)
            throws InstrumentNotSupportedException;

    /**
     * Возвращает котировку.
     *
     * @throws InstrumentNotSupportedException если подходящий обработчик инструмента не найден
     */
    Publisher<QuoteTick> getQuote(Instrument instrument) throws InstrumentNotSupportedException;
}
