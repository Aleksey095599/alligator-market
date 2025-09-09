package com.alligator.market.domain.provider.handler.registry;

import com.alligator.market.domain.instrument.base.contract.Instrument;
import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.provider.handler.contract.InstrumentHandler;

/**
 * Реестр обработчиков.
 */
public interface InstrumentHandlerRegistry {

    /** Находит обработчик по классу инструмента. Пусто, если не зарегистрирован. */
    <P extends MarketDataProvider, I extends Instrument>
    java.util.Optional<InstrumentHandler<P, I>> find(java.lang.Class<I> instrumentClass);

    /** Возвращает обработчик или бросает IllegalStateException. */
    <P extends MarketDataProvider, I extends Instrument>
    InstrumentHandler<P, I> getOrThrow(java.lang.Class<I> instrumentClass);
}
