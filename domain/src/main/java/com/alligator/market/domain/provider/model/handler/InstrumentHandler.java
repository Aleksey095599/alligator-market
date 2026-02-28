package com.alligator.market.domain.provider.model.handler;

import com.alligator.market.domain.instrument.model.Instrument;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.provider.model.MarketDataProvider;
import com.alligator.market.domain.provider.model.vo.HandlerCode;
import com.alligator.market.domain.quote.tick.model.QuoteTick;
import org.reactivestreams.Publisher;

import java.util.Set;

/**
 * Обработчик (handler) финансового инструмента.
 *
 * <p>Примечание: бизнес-логика предполагает, что обработчик жёстко привязан к одному конкретному провайдеру и
 * нацелен на работу с одним конкретным классом/типом финансового инструмента.</p>
 */
public sealed interface InstrumentHandler<P extends MarketDataProvider, I extends Instrument>
        permits AbstractInstrumentHandler {

    /**
     * Код обработчика.
     */
    HandlerCode handlerCode();

    /**
     * Класс поддерживаемых инструментов.
     */
    Class<I> instrumentClass();

    /**
     * Тип поддерживаемых инструментов.
     */
    InstrumentType instrumentType();

    /**
     * Коды поддерживаемых инструментов.
     */
    Set<InstrumentCode> supportedInstrumentCodes();

    /**
     * Прикрепление обработчика к провайдеру.
     */
    void attachTo(P provider);

    /**
     * Поток котировок для заданного инструмента.
     */
    Publisher<QuoteTick> quote(I instrument);
}
