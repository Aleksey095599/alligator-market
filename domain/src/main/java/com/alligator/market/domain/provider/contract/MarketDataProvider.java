package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.base.contract.Instrument;
import com.alligator.market.domain.provider.exception.InstrumentNotSupportedException;
import com.alligator.market.domain.provider.profile.model.Profile;
import com.alligator.market.domain.quote.QuoteTick;
import org.reactivestreams.Publisher;

import java.util.Map;

/**
 * Контракт провайдера рыночных данных.
 */
public interface MarketDataProvider {

    /** Возвращает профиль провайдера {@link Profile}. */
    Profile getProfile();

    /** Возвращает карту обработчиков по инструментам. */
    Map<Instrument, InstrumentHandler<? extends MarketDataProvider, ? extends Instrument>> getHandlers();

    /**
     * Возвращает котировку.
     *
     * @throws InstrumentNotSupportedException если подходящий обработчик инструмента не найден
     */
    Publisher<QuoteTick> getQuote(Instrument instrument) throws InstrumentNotSupportedException;
}
