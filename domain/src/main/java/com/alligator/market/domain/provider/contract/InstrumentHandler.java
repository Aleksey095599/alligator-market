package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.model.Instrument;
import com.alligator.market.domain.instrument.model.InstrumentType;
import com.alligator.market.domain.quote.QuoteTick;
import reactor.core.publisher.Flux;

/**
 * Контракт обработчика (handler) для конкретного инструмента.
 */
public interface InstrumentHandler {

    /** Возвращает поддерживаемый тип инструмента. */
    InstrumentType supportedInstrument();

    /** Возвращает котировку для указанного инструмента. */
    Flux<QuoteTick> instrumentQuote(Instrument instrument);
}
