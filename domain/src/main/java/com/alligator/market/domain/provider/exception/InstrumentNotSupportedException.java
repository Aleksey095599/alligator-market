package com.alligator.market.domain.provider.exception;

import com.alligator.market.domain.common.exception.BaseDomainException;
import com.alligator.market.domain.common.exception.DomainErrorCode;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.provider.model.vo.HandlerCode;

import java.util.Objects;

/**
 * Инструмент не поддерживается обработчиком.
 */
public final class InstrumentNotSupportedException extends BaseDomainException {

    private final InstrumentCode instrumentCode;
    private final HandlerCode handlerCode;

    /**
     * Создает исключение.
     *
     * @param instrumentCode код инструмента, который не поддерживается
     * @param handlerCode    код обработчика, который не поддерживает {@code instrumentCode}
     */
    @SuppressWarnings("unused")
    public InstrumentNotSupportedException(InstrumentCode instrumentCode, HandlerCode handlerCode) {
        super(DomainErrorCode.INSTRUMENT_NOT_SUPPORTED, msg(instrumentCode, handlerCode));
        this.instrumentCode = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        this.handlerCode = Objects.requireNonNull(handlerCode, "handlerCode must not be null");
    }

    /**
     * Создает исключение с причиной.
     *
     * @param instrumentCode код инструмента, который не поддерживается
     * @param handlerCode    код обработчика, который не поддерживает {@code instrumentCode}
     * @param cause          причина ошибки
     */
    @SuppressWarnings("unused")
    public InstrumentNotSupportedException(InstrumentCode instrumentCode, HandlerCode handlerCode, Throwable cause) {
        super(DomainErrorCode.INSTRUMENT_NOT_SUPPORTED, msg(instrumentCode, handlerCode), cause);
        this.instrumentCode = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        this.handlerCode = Objects.requireNonNull(handlerCode, "handlerCode must not be null");
    }

    /**
     * Формирует сообщение об ошибке.
     *
     * @param instrumentCode код инструмента, который не поддерживается
     * @param handlerCode    код обработчика, который не поддерживает {@code instrumentCode}
     * @return текст сообщения
     */
    private static String msg(InstrumentCode instrumentCode, HandlerCode handlerCode) {
        InstrumentCode ic = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        HandlerCode hc = Objects.requireNonNull(handlerCode, "handlerCode must not be null");
        return "Instrument not supported (instrumentCode=" + ic.value() + ", handlerCode=" + hc.value() + ")";
    }

    /**
     * Возвращает код инструмента.
     *
     * @return код инструмента
     */
    @SuppressWarnings("unused")
    public InstrumentCode getInstrumentCode() {
        return instrumentCode;
    }

    /**
     * Возвращает код обработчика.
     *
     * @return код обработчика
     */
    @SuppressWarnings("unused")
    public HandlerCode getHandlerCode() {
        return handlerCode;
    }
}
