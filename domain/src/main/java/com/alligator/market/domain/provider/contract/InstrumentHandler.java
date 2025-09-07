package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.base.Instrument;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.quote.QuoteTick;
import org.reactivestreams.Publisher;

/**
 * Контракт обработчика (handler) для конкретного финансового инструмента.
 * Каждый провайдер рыночных данных {@link MarketDataProvider} должен иметь как минимум один обработчик.
 */
public interface InstrumentHandler<P extends MarketDataProvider> {

    /** Возвращает внутренний код провайдера рыночных данных, к которому относится обработчик. */
    String getProviderCode();

    /** Возвращает провайдера, к которому относится обработчик. */
    P getProvider();

    /** Возвращает тип финансового инструмента, который поддерживает данный обработчик. */
    InstrumentType getSupportedInstrumentType();

    /** Возвращает котировку для заданного инструмента. */
    Publisher<QuoteTick> getInstrumentQuote(Instrument instrument);
}
