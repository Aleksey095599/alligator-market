package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.base.contract.Instrument;
import com.alligator.market.domain.provider.handler.contract.InstrumentHandler;
import com.alligator.market.domain.provider.profile.model.Profile;

import java.util.Set;

/**
 * Контракт провайдера рыночных данных.
 */
public interface MarketDataProvider {

    /** Профиль провайдера. */
    Profile profile();

    /** Набор обработчиков. */
    Set<InstrumentHandler<? extends MarketDataProvider, ? extends Instrument>> handlers();

    /** Возвращает обработчик для указанного инструмента. */

}
