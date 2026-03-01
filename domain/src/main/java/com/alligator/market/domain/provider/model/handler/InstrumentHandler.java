package com.alligator.market.domain.provider.model.handler;

import com.alligator.market.domain.instrument.model.Instrument;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.provider.model.MarketDataProvider;
import com.alligator.market.domain.provider.model.vo.HandlerCode;
import com.alligator.market.domain.quote.tick.model.QuoteTick;
import org.reactivestreams.Publisher;

import java.util.Objects;
import java.util.Set;

/**
 * Обработчик (handler) финансового инструмента {@link Instrument} для заданного провайдера {@link MarketDataProvider}.
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

    /**
     * Поддерживается ли конкретный код инструмента (дефолтная реализация).
     */
    default boolean supportsByCode(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        return supportedInstrumentCodes().contains(instrumentCode);
    }

    /**
     * Производная проекция: поддерживается ли конкретный инструмент с точки зрения контракта обработчика
     * (класс + тип + код + membership в supported-наборе).
     */
    default boolean supports(Instrument instrument) {
        if (instrument == null) {
            return false;
        }
        if (!instrumentClass().isInstance(instrument)) {
            return false;
        }
        if (instrument.instrumentType() != instrumentType()) {
            return false;
        }
        InstrumentCode code = instrument.instrumentCode();
        return code != null && supportedInstrumentCodes().contains(code);
    }
}
