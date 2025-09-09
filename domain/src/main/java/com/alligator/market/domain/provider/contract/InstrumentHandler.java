package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.base.contract.Instrument;
import com.alligator.market.domain.quote.QuoteTick;
import org.reactivestreams.Publisher;

import java.util.Set;

/**
 * Контракт обработчика конкретного финансового инструмента.
 * Жестко привязан к конкретному провайдеру {@link MarketDataProvider}.
 * Жестко привязан к классу финансового инструмента.
 */
public interface InstrumentHandler<P extends MarketDataProvider, I extends Instrument> {

    /** Возвращает провайдера, к которому относится обработчик. */
    P getProvider();

    /** Возвращает класс поддерживаемых инструментов.  */
    Class<I> getInstrumentClass();

    /** Возвращает набор поддерживаемых инструментов. */
    Set<I> getSupportedInstruments();

    /** Проверяет, поддерживается ли заданный инструмент:
     * 1) принадлежит ли классу {@link #getInstrumentClass()};
     * 2) принадлежит ли набору {@link #getSupportedInstruments()}.
     */
    default boolean supportedInstrument(I instrument) {

    }

    /** Возвращает котировку заданного инструмента. */
    Publisher<QuoteTick> getQuote(I instrument);
}
