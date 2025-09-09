package com.alligator.market.domain.provider.handler.contract;

import com.alligator.market.domain.instrument.base.contract.Instrument;
import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.quote.QuoteTick;
import org.reactivestreams.Publisher;

import java.util.Objects;
import java.util.Set;

/**
 * Контракт обработчика финансового инструмента.
 * Жёстко привязан к провайдеру и к классу инструмента.
 */
public interface InstrumentHandler<P extends MarketDataProvider, I extends Instrument> {

    /** Провайдер, к которому относится обработчик. */
    P provider();

    /** Класс поддерживаемых инструментов. */
    Class<I> instrumentClass();

    /** Набор поддерживаемых инструментов. */
    Set<I> supportedInstruments(); // TODO не должно быть дублирований

    /** Котировка заданного инструмента. */
    Publisher<QuoteTick> quote(I instrument);
    // TODO инструмент не пустой, можно в виде Objects.requireNonNull(instrument, "instrument must not be null")
    // TODO инструмент должен принадлежать классу instrumentClass иначе ошибка InstrumentWrongClassException
    // TODO инструмент должен принадлежать набору supportedInstruments иначе ошибка InstrumentNotSupported
}
