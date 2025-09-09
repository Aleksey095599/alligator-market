package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.base.contract.Instrument;
import com.alligator.market.domain.provider.exception.InstrumentNotSupportedException;
import com.alligator.market.domain.provider.handler.contract.InstrumentHandler;
import com.alligator.market.domain.provider.profile.model.Profile;

/**
 * Контракт провайдера рыночных данных.
 */
public interface MarketDataProvider {

    /** Профиль провайдера {@link Profile}. */
    Profile profile();

    /** Набор обработчиков. */
    Set<> handlers();

    /**
     * Возвращает обработчик для указанного инструмента.
     *
     * @throws InstrumentNotSupportedException если обработчик для инструмента не найден
     */
    InstrumentHandler<? extends MarketDataProvider, ? extends Instrument> getHandler(Instrument instrument)
            throws HandlerNotFoundException;
}
