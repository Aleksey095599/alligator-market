package com.alligator.market.domain.provider.handler.contract;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.quote.QuoteTick;
import reactor.core.publisher.Flux;

/**
 * Контракт обработчика (handler) для конкретного финансового инструмента.
 * Каждый провайдер рыночных данных {@link MarketDataProvider} имеет как минимум один обработчик.
 */
public interface InstrumentHandler {

    /** Возвращает внутренний код провайдера рыночных данных, к которому относится обработчик. */
    String getProviderCode();

    /** Возвращает тип финансового инструмента, который поддерживает данный обработчик. */
    InstrumentType getSupportedInstrumentType();

    /** Возвращает котировку для заданного инструмента. */
    Flux<QuoteTick> getInstrumentQuote(Instrument instrument);
}
