package com.alligator.market.domain.provider.exception;

import com.alligator.market.domain.instrument.type.InstrumentType;

import java.util.Objects;

/**
 * Тип инструмента не соответствует обработчику.
 */
public final class InstrumentWrongTypeException extends RuntimeException {

    private final String instrumentCode;
    private final InstrumentType instrumentType;
    private final String handlerCode;
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
            String instrumentCode,
            InstrumentType instrumentType,
            String handlerCode,
            InstrumentType expectedType
    ) {
        super(msg(instrumentCode, instrumentType, handlerCode, expectedType));
        this.instrumentCode = instrumentCode;
        this.instrumentType = instrumentType;
        this.handlerCode = handlerCode;
        this.expectedType = expectedType;
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
            String instrumentCode,
            InstrumentType instrumentType,
            String handlerCode,
            InstrumentType expectedType,
            Throwable cause
    ) {
        super(msg(instrumentCode, instrumentType, handlerCode, expectedType), cause);
        this.instrumentCode = instrumentCode;
        this.instrumentType = instrumentType;
        this.handlerCode = handlerCode;
        this.expectedType = expectedType;
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
            String instrumentCode,
            InstrumentType instrumentType,
            String handlerCode,
            InstrumentType expectedType
    ) {
        String ic = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        InstrumentType actual = Objects.requireNonNull(instrumentType, "instrumentType must not be null");
        String hc = Objects.requireNonNull(handlerCode, "handlerCode must not be null");
        InstrumentType expected = Objects.requireNonNull(expectedType, "expectedType must not be null");
        return "Instrument type mismatch (instrumentCode=" + ic + ", actualType=" + actual.name()
                + ", handlerCode=" + hc + ", expectedType=" + expected.name() + ")";
    }

    /**
     * Возвращает код инструмента.
     *
     * @return код инструмента
     */
    @SuppressWarnings("unused")
    public String getInstrumentCode() {
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
    public String getHandlerCode() {
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
