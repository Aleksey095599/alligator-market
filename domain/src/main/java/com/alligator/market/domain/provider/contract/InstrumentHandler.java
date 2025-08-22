package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.instrument.contract.InstrumentType;
import com.alligator.market.domain.quote.QuoteTick;
import reactor.core.publisher.Flux;

/**
 * Контракт обработчика (handler) конкретного финансового инструмента.
 * Обработчики применяются провайдерами рыночных данных {@link MarketDataProvider} для обработки
 * запросов котировок для разных инструментов.
 */
public interface InstrumentHandler {

    /** Возвращает код провайдера рыночных данных, к которому относится обработчик. */
    String providerCode();

    /** Возвращает поддерживаемый тип инструмента. */
    InstrumentType supportedInstrument();

    /** Возвращает котировку для указанного инструмента. */
    Flux<QuoteTick> instrumentQuote(Instrument instrument);
}
