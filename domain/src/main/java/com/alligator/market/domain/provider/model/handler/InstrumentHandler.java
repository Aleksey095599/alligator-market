package com.alligator.market.domain.provider.model.handler;

import com.alligator.market.domain.instrument.model.Instrument;
import com.alligator.market.domain.instrument.asset.InstrumentType;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.provider.model.MarketDataProvider;
import com.alligator.market.domain.provider.model.vo.HandlerCode;
import com.alligator.market.domain.quote.tick.model.QuoteTick;
import org.reactivestreams.Publisher;

import java.util.Set;

/**
 * Обработчик (handler) финансового инструмента {@link Instrument} для заданного провайдера {@link MarketDataProvider}.
 */
public interface InstrumentHandler<P extends MarketDataProvider, I extends Instrument> {

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
     * Признак: инструмент сопоставим с обработчиком по доменным признакам (класс + тип).
     */
    default boolean isCompatible(Instrument instrument) {
        return instrument != null
                && instrumentClass().isInstance(instrument)
                && instrument.instrumentType() == instrumentType();
    }

    /**
     * Коды поддерживаемых инструментов.
     */
    Set<InstrumentCode> supportedInstrumentCodes();

    /**
     * Признак: поддерживается ли конкретный код инструмента.
     */
    default boolean isSupported(InstrumentCode instrumentCode) {
        return instrumentCode != null && supportedInstrumentCodes().contains(instrumentCode);
    }

    /**
     * Прикрепление обработчика к провайдеру.
     */
    void attachTo(P provider);

    /**
     * Признак: обработчик уже прикреплён к провайдеру.
     *
     * <p>Назначение: Одно из требований корректного состояния обработчика – прикрепление к провайдеру.
     * Перед выполнением метода получения потока котировок, обработчик проверяет прикрепление к провайдеру.</p>
     */
    boolean isAttached();

    /**
     * Поток котировок для заданного инструмента.
     */
    Publisher<QuoteTick> quote(I instrument);
}
