package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.instrument.contract.InstrumentType;
import com.alligator.market.domain.quote.QuoteTick;
import reactor.core.publisher.Flux;

/**
 * Контракт обработчика (handler) для конкретного финансового инструмента.
 * Каждый провайдер рыночных данных {@link MarketDataProvider} имеет как минимум один обработчик.
 */
public interface InstrumentHandler {

    /** Возвращает код провайдера рыночных данных, к которому относится обработчик. */
    String getProviderCode();

    /** Возвращает финансовый инструмент, который поддерживает данный обработчик. */
    Instrument getInstrument();

    /** Возвращает котировку для указанного инструмента. */
    Flux<QuoteTick> getInstrumentQuote();
}
