package com.alligator.market.domain.provider.handler.contract;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.provider.handler.base.AbstractInstrumentHandler;
import com.alligator.market.domain.quote.QuoteTick;
import org.reactivestreams.Publisher;

import java.util.Set;

/**
 * Контракт обработчика финансового инструмента.
 * Жёстко привязан к провайдеру и конкретному классу инструмента.
 * Реализация разрешена только через {@link AbstractInstrumentHandler}.
 */
public sealed interface InstrumentHandler<P extends MarketDataProvider, I extends Instrument>
        permits AbstractInstrumentHandler {

    /** Уникальный код обработчика (для логов/метрик). */
    String handlerCode();

    /** Декларируем класс поддерживаемых инструментов. */
    Class<I> instrumentClass();

    /** Набор инструментов, которые поддерживает обработчик. */
    Set<I> supportedInstruments();

    /** Прикрепить обработчик к заданному провайдеру. */
    void attachTo(P provider);

    /** Котировка заданного инструмента. */
    Publisher<QuoteTick> quote(I instrument);
}
