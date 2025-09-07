package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.base.Instrument;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.quote.QuoteTick;
import org.reactivestreams.Publisher;

import java.util.Set;

/**
 * Контракт обработчика (handler) для конкретного набора финансовых инструментов одного типа.
 */
public interface InstrumentHandler<P extends MarketDataProvider> {

    /** Возвращает провайдера, к которому относится обработчик. */
    P getProvider();

    /** Возвращает тип финансового инструмента, который поддерживает данный обработчик. */
    InstrumentType getSupportedInstrumentType();

    /** Возвращает набор поддерживаемых инструментов. */
    Set<Instrument> getSupportedInstruments();

    /** Проверяет поддержку указанного инструмента. */
    boolean supportsInstrument(Instrument instrument);

    /** Возвращает котировку для заданного инструмента. */
    Publisher<QuoteTick> getInstrumentQuote(Instrument instrument);
}
