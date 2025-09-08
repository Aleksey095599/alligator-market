package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.base.contract.Instrument;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.quote.QuoteTick;
import org.reactivestreams.Publisher;

import java.util.Set;

/**
 * Контракт обработчика (handler) для конкретного набора финансовых инструментов одного типа.
 */
public interface InstrumentHandler<P extends MarketDataProvider, I extends Instrument> {

    /** Возвращает провайдера, к которому относится обработчик. */
    P getProvider();

    /** Возвращает тип финансового инструмента, который поддерживает данный обработчик. */
    InstrumentType getSupportedInstrumentType();

    /** Возвращает набор поддерживаемых инструментов. */
    Set<I> getSupportedInstruments();

    /** Проверяет поддержку указанного инструмента. */
    boolean supportsInstrument(I instrument);

    /** Возвращает котировку для заданного инструмента. */
    Publisher<QuoteTick> getInstrumentQuote(I instrument);
}
