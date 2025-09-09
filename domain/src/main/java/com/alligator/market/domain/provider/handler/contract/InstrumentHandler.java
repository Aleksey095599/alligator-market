package com.alligator.market.domain.provider.handler.contract;

import com.alligator.market.domain.instrument.base.contract.Instrument;
import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.quote.QuoteTick;
import org.reactivestreams.Publisher;

import java.util.Set;

/**
 * Контракт обработчика финансового инструмента.
 * Жёстко привязан к провайдеру и к классу инструмента.
 */
public interface InstrumentHandler<P extends MarketDataProvider, I extends Instrument> {

    /** Класс поддерживаемых инструментов. */
    Class<I> instrumentClass();

    /** Набор поддерживаемых инструментов. */
    Set<I> supportedInstruments();

    /** Котировка заданного инструмента. */
    Publisher<QuoteTick> quote(I instrument);
}
