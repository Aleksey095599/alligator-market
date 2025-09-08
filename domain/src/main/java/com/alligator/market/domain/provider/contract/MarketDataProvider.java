package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.instrument.type.InstrumentType;
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

    /** Возвращает карту обработчиков {@link InstrumentHandler}. */
    Map<InstrumentType, InstrumentHandler<? extends MarketDataProvider, ? extends Instrument>> getHandlers();

    /**
     * Возвращает котировку.
     *
     * @throws InstrumentNotSupportedException если подходящий обработчик инструмента не найден
     */
    Publisher<QuoteTick> getQuote(Instrument instrument) throws InstrumentNotSupportedException;
}
