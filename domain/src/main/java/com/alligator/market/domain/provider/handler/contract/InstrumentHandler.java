package com.alligator.market.domain.provider.handler.contract;

import com.alligator.market.domain.instrument.base.contract.Instrument;
import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.quote.QuoteTick;
import org.reactivestreams.Publisher;

import java.util.Objects;
import java.util.Set;

/**
 * Контракт обработчика конкретного финансового инструмента.
 * Жёстко привязан к провайдеру и к классу инструмента.
 */
public interface InstrumentHandler<P extends MarketDataProvider, I extends Instrument> {

    /** Провайдер, к которому относится обработчик. */
    P provider();

    /** Класс поддерживаемых инструментов. */
    Class<I> instrumentClass();

    /** Явно поддерживаемые инструменты. Если пусто значит  */
    Set<I> supportedInstruments();

    /** Котировка заданного инструмента. */
    Publisher<QuoteTick> quote(I instrument);

    /** Проверяет поддержку указанного инструмента. */
    default boolean supports(I instrument) {
        Objects.requireNonNull(instrument, "instrument must not be null");
        if (!instrumentClass().isInstance(instrument)) { // Проверяем корректный ли класс
            return false;
        }
        Set<I> supported = supportedInstruments();
        if (supported.isEmpty()) { // Проверяем наличие в списке поддерживаемых инструментов
            return false;
        }
        return supported.contains(instrument);
    }
}
