package com.alligator.market.domain.provider.handler.contract;

import com.alligator.market.domain.instrument.base.contract.Instrument;
import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.quote.QuoteTick;
import org.reactivestreams.Publisher;

import java.util.Set;

/**
 * Контракт обработчика финансового инструмента.
 * Жёстко привязан к провайдеру и конкретному классу инструмента.
 */
public interface InstrumentHandler<P extends MarketDataProvider, I extends Instrument> {

    /** Уникальный код обработчика (для логов/метрик). */
    String handlerCode();

    /** Класс поддерживаемого инструмента. */
    Class<I> instrumentClass();

    /** Конкретные инструменты, которые поддерживает обработчик. */
    Set<I> supportedInstruments();

    /** Прикрепить обработчик к заданному провайдеру. */
    void attachTo(P provider);

    /** Котировка заданного инструмента. */
    Publisher<QuoteTick> quote(I instrument);
}
