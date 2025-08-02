package com.alligator.market.domain.provider.model;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.InstrumentType;
import com.alligator.market.domain.quote.QuoteTick;
import reactor.core.publisher.Flux;

/**
 * Контракт обработчика (handler) для конкретного инструмента.
 */
public interface InstrumentHandler {

    /** Возвращает поддерживаемый тип инструмента. */
    InstrumentType supportedType();

    /** Возвращает котировку для указанного инструмента. */
    Flux<QuoteTick> thisInstrumentQuote(Instrument instrument);
}
