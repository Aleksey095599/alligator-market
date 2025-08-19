package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.model.Instrument;
import com.alligator.market.domain.instrument.model.InstrumentType;
import com.alligator.market.domain.quote.QuoteTick;
import reactor.core.publisher.Flux;

/**
 * Контракт обработчика (handler) для конкретного инструмента.
 *
 * @param <P> провайдер рыночных данных
 */
public interface InstrumentHandler<P extends MarketDataProvider<?>> {

    /** Возвращает поддерживаемый тип инструмента. */
    InstrumentType supportedInstrument();

    /** Возвращает котировку для указанного инструмента. */
    Flux<QuoteTick> instrumentQuote(Instrument instrument);
}
