package com.alligator.market.domain.provider.contract.handler;

import com.alligator.market.domain.instrument.code.InstrumentCode;
import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.quote.tick.model.QuoteTick;
import org.reactivestreams.Publisher;

import java.util.Set;

/**
 * Обработчик финансового инструмента.
 *
 * <p>Жёстко привязан к провайдеру и конкретному классу инструмента. Реализация разрешена только
 * через {@link AbstractInstrumentHandler}.</p>
 */
public sealed interface InstrumentHandler<P extends MarketDataProvider, I extends Instrument>
        permits AbstractInstrumentHandler {

    /**
     * Уникальный код обработчика.
     */
    String handlerCode();

    /**
     * Декларируем класс поддерживаемых инструментов.
     */
    Class<I> instrumentClass();

    /**
     * Декларируемый тип поддерживаемых инструментов.
     */
    InstrumentType instrumentType();

    /**
     * Неизменяемый набор поддерживаемых кодов инструментов.
     */
    Set<InstrumentCode> supportedInstrumentCodes();

    /**
     * Однократное прикрепление обработчика к провайдеру.
     */
    void attachTo(P provider);

    /**
     * Поток котировок для заданного инструмента (возможен Mono как частный случай).
     */
    Publisher<QuoteTick> quote(I instrument);
}
