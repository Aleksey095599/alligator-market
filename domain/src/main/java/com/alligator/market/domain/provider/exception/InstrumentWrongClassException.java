package com.alligator.market.domain.provider.exception;

import com.alligator.market.domain.instrument.code.InstrumentCode;

import java.util.Objects;

/**
 * Класс инструмента не соответствует обработчику.
 */
public final class InstrumentWrongClassException extends RuntimeException {

    private final InstrumentCode instrumentCode;
    private final Class<?> instrumentClass;
    private final String handlerCode;
    private final Class<?> expectedClass;

    /**
     * Создает исключение.
     *
     * @param instrumentCode  код инструмента
     * @param instrumentClass фактический класс инструмента
     * @param handlerCode     код обработчика
     * @param expectedClass   ожидаемый класс инструмента
     */
    public InstrumentWrongClassException(
            InstrumentCode instrumentCode,
            Class<?> instrumentClass,
            String handlerCode,
            Class<?> expectedClass
    ) {
        super(msg(instrumentCode, instrumentClass, handlerCode, expectedClass));
        this.instrumentCode = instrumentCode;
        this.instrumentClass = instrumentClass;
        this.handlerCode = handlerCode;
        this.expectedClass = expectedClass;
    }

    /**
     * Создает исключение с причиной.
     *
     * @param instrumentCode  код инструмента
     * @param instrumentClass фактический класс инструмента
     * @param handlerCode     код обработчика
     * @param expectedClass   ожидаемый класс инструмента
     * @param cause           причина ошибки
     */
    @SuppressWarnings("unused")
    public InstrumentWrongClassException(
            InstrumentCode instrumentCode,
            Class<?> instrumentClass,
            String handlerCode,
            Class<?> expectedClass,
            Throwable cause
    ) {
        super(msg(instrumentCode, instrumentClass, handlerCode, expectedClass), cause);
        this.instrumentCode = instrumentCode;
        this.instrumentClass = instrumentClass;
        this.handlerCode = handlerCode;
        this.expectedClass = expectedClass;
    }

    /**
     * Формирует сообщение об ошибке.
     *
     * @param instrumentCode  код инструмента
     * @param instrumentClass фактический класс инструмента
     * @param handlerCode     код обработчика
     * @param expectedClass   ожидаемый класс инструмента
     * @return текст сообщения
     */
    private static String msg(
            InstrumentCode instrumentCode,
            Class<?> instrumentClass,
            String handlerCode,
            Class<?> expectedClass
    ) {
        InstrumentCode ic = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Class<?> actual = Objects.requireNonNull(instrumentClass, "instrumentClass must not be null");
        String hc = Objects.requireNonNull(handlerCode, "handlerCode must not be null");
        Class<?> expected = Objects.requireNonNull(expectedClass, "expectedClass must not be null");
        return "Instrument class mismatch (instrumentCode=" + ic.value() + ", actualClass=" + actual.getName()
                + ", handlerCode=" + hc + ", expectedClass=" + expected.getName() + ")";
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
     * Возвращает фактический класс инструмента.
     *
     * @return фактический класс инструмента
     */
    @SuppressWarnings("unused")
    public Class<?> getInstrumentClass() {
        return instrumentClass;
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
     * Возвращает ожидаемый класс инструмента.
     *
     * @return ожидаемый класс инструмента
     */
    @SuppressWarnings("unused")
    public Class<?> getExpectedClass() {
        return expectedClass;
    }
}
