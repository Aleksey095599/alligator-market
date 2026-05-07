package com.alligator.market.domain.provider.handler;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.tick.level.source.SourceMarketDataTick;
import com.alligator.market.domain.provider.MarketDataSource;
import com.alligator.market.domain.provider.vo.HandlerCode;
import org.reactivestreams.Publisher;

import java.util.Set;

/**
 * Внутренний SPI обработчика финансовых инструментов заданного типа, прикрепляемый к заданному провайдеру.
 *
 * <p>Примечание: Обработчик за счёт дженериков параметризован конкретным классом провайдера и классом финансового
 * инструмента, что отражает прикрепление к заданному провайдеру и соответствие заданному типу инструмента.</p>
 */
public interface InstrumentHandler<P extends MarketDataSource, I extends Instrument> {

    /**
     * Уникальный код обработчика.
     */
    HandlerCode handlerCode();

    /**
     * Коды поддерживаемых инструментов.
     */
    Set<InstrumentCode> supportedInstrumentCodes();

    /**
     * Прикрепляет обработчик к провайдеру.
     */
    void attachTo(P provider);

    /**
     * Streams source-level market data ticks for the given instrument.
     */
    Publisher<SourceMarketDataTick> streamSourceTicks(I instrument);
}
