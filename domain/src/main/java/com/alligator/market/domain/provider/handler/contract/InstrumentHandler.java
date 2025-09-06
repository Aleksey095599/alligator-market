package com.alligator.market.domain.provider.handler.contract;

import com.alligator.market.domain.instrument.base.Instrument;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.quote.QuoteTick;
import org.reactivestreams.Publisher;

/**
 * Контракт обработчика (handler) для конкретного финансового инструмента.
 * Каждый провайдер рыночных данных {@link MarketDataProvider} должен иметь как минимум один обработчик.
 */
public interface InstrumentHandler {

    /** Возвращает внутренний код провайдера рыночных данных, к которому относится обработчик. */
    String getProviderCode();

    /** Возвращает тип финансового инструмента, который поддерживает данный обработчик. */
    InstrumentType getSupportedInstrumentType();

    /** Возвращает котировку для заданного инструмента. */
    Publisher<QuoteTick> getInstrumentQuote(Instrument instrument);
}
