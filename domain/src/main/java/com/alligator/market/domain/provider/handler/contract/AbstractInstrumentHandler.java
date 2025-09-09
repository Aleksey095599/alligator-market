package com.alligator.market.domain.provider.handler.contract;

import com.alligator.market.domain.instrument.base.contract.Instrument;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.quote.QuoteTick;
import org.reactivestreams.Publisher;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Абстрактный каркас обработчика инструмента.
 */
public abstract class AbstractInstrumentHandler<P extends MarketDataProvider, I extends Instrument>
        implements InstrumentHandler<P, I> {

    /** Провайдер, к которому относится обработчик. */
    private P provider;

    /** Класс поддерживаемых инструментов. */

    /** Набор поддерживаемых инструментов. */
    private final Set<I> supportedInstruments; // TODO не должно быть дублирований

    /** Котировка заданного инструмента. */
    Publisher<QuoteTick> quote(I instrument);
    // TODO инструмент не пустой, можно в виде Objects.requireNonNull(instrument, "instrument must not be null")
    // TODO инструмент должен принадлежать классу instrumentClass иначе ошибка InstrumentWrongClassException
    // TODO инструмент должен принадлежать набору supportedInstruments иначе ошибка InstrumentNotSupported

}
