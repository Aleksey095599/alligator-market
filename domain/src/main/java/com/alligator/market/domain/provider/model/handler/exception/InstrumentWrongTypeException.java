package com.alligator.market.domain.provider.model.handler.exception;

import com.alligator.market.domain.common.exception.BaseDomainException;
import com.alligator.market.domain.common.exception.DomainErrorCode;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.instrument.model.InstrumentType;
import com.alligator.market.domain.provider.model.vo.HandlerCode;

import java.util.Objects;

/**
 * Тип инструмента не соответствует обработчику.
 */
public final class InstrumentWrongTypeException extends BaseDomainException {

    private final InstrumentCode instrumentCode;
    private final InstrumentType instrumentType;
    private final HandlerCode handlerCode;
    private final InstrumentType expectedType;

    /**
     * Создает исключение.
     *
     * @param instrumentCode код инструмента
     * @param instrumentType фактический тип инструмента
     * @param handlerCode    код обработчика
     * @param expectedType   ожидаемый тип инструмента
     */
    public InstrumentWrongTypeException(
            InstrumentCode instrumentCode,
            InstrumentType instrumentType,
            HandlerCode handlerCode,
            InstrumentType expectedType
    ) {
        super(
                DomainErrorCode.INSTRUMENT_WRONG_TYPE,
                msg(instrumentCode, instrumentType, handlerCode, expectedType)
        );
        this.instrumentCode = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        this.instrumentType = Objects.requireNonNull(instrumentType, "instrumentType must not be null");
        this.handlerCode = Objects.requireNonNull(handlerCode, "handlerCode must not be null");
        this.expectedType = Objects.requireNonNull(expectedType, "expectedType must not be null");
    }

    /**
     * Создает исключение с причиной.
     *
     * @param instrumentCode код инструмента
     * @param instrumentType фактический тип инструмента
     * @param handlerCode    код обработчика
     * @param expectedType   ожидаемый тип инструмента
     * @param cause          причина ошибки
     */
    @SuppressWarnings("unused")
    public InstrumentWrongTypeException(
            InstrumentCode instrumentCode,
            InstrumentType instrumentType,
            HandlerCode handlerCode,
            InstrumentType expectedType,
            Throwable cause
    ) {
        super(
                DomainErrorCode.INSTRUMENT_WRONG_TYPE,
                msg(instrumentCode, instrumentType, handlerCode, expectedType),
                cause
        );
        this.instrumentCode = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        this.instrumentType = Objects.requireNonNull(instrumentType, "instrumentType must not be null");
        this.handlerCode = Objects.requireNonNull(handlerCode, "handlerCode must not be null");
        this.expectedType = Objects.requireNonNull(expectedType, "expectedType must not be null");
    }

    /**
     * Формирует сообщение об ошибке.
     *
     * @param instrumentCode код инструмента
     * @param instrumentType фактический тип инструмента
     * @param handlerCode    код обработчика
     * @param expectedType   ожидаемый тип инструмента
     * @return текст сообщения
     */
    private static String msg(
            InstrumentCode instrumentCode,
            InstrumentType instrumentType,
            HandlerCode handlerCode,
            InstrumentType expectedType
    ) {
        InstrumentCode ic = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        InstrumentType actual = Objects.requireNonNull(instrumentType, "instrumentType must not be null");
        HandlerCode hc = Objects.requireNonNull(handlerCode, "handlerCode must not be null");
        InstrumentType expected = Objects.requireNonNull(expectedType, "expectedType must not be null");
        return "Instrument type mismatch (instrumentCode=" + ic.value() + ", actualType=" + actual.name()
                + ", handlerCode=" + hc.value() + ", expectedType=" + expected.name() + ")";
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
     * Возвращает фактический тип инструмента.
     *
     * @return фактический тип инструмента
     */
    @SuppressWarnings("unused")
    public InstrumentType getInstrumentType() {
        return instrumentType;
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

    /**
     * Возвращает ожидаемый тип инструмента.
     *
     * @return ожидаемый тип инструмента
     */
    @SuppressWarnings("unused")
    public InstrumentType getExpectedType() {
        return expectedType;
    }
}
