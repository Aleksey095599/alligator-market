package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.base.Instrument;
import com.alligator.market.domain.provider.exception.InstrumentNotSupportedException;
import com.alligator.market.domain.provider.profile.model.Profile;
import com.alligator.market.domain.quote.QuoteTick;
import com.alligator.market.domain.instrument.type.InstrumentType;
import java.util.Map;

import org.reactivestreams.Publisher;

/**
 * Контракт провайдера рыночных данных.
 */
public interface MarketDataProvider {

    /** Возвращает профиль провайдера {@link Profile}. */
    Profile getProfile();

    /** Возвращает карту обработчиков {@link InstrumentHandler}. */
    Map<InstrumentType, InstrumentHandler> getHandlers();

    /**
     * Возвращает котировку.
     *
     * @throws InstrumentNotSupportedException если подходящий обработчик инструмента не найден
     */
    Publisher<QuoteTick> getQuote(Instrument instrument) throws InstrumentNotSupportedException;
}
