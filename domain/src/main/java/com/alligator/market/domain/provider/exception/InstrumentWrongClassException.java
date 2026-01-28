package com.alligator.market.domain.provider.exception;

import com.alligator.market.domain.common.exception.BaseDomainException;
import com.alligator.market.domain.common.exception.DomainErrorCode;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.provider.model.vo.HandlerCode;

import java.util.Objects;

/**
 * Класс инструмента не соответствует обработчику.
 */
public final class InstrumentWrongClassException extends BaseDomainException {

    private final InstrumentCode instrumentCode;
    private final Class<?> instrumentClass;
    private final HandlerCode handlerCode;
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
            HandlerCode handlerCode,
            Class<?> expectedClass
    ) {
        super(
                DomainErrorCode.INSTRUMENT_WRONG_CLASS,
                msg(instrumentCode, instrumentClass, handlerCode, expectedClass)
        );
        this.instrumentCode = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        this.instrumentClass = Objects.requireNonNull(instrumentClass, "instrumentClass must not be null");
        this.handlerCode = Objects.requireNonNull(handlerCode, "handlerCode must not be null");
        this.expectedClass = Objects.requireNonNull(expectedClass, "expectedClass must not be null");
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
            HandlerCode handlerCode,
            Class<?> expectedClass,
            Throwable cause
    ) {
        super(
                DomainErrorCode.INSTRUMENT_WRONG_CLASS,
                msg(instrumentCode, instrumentClass, handlerCode, expectedClass),
                cause
        );
        this.instrumentCode = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        this.instrumentClass = Objects.requireNonNull(instrumentClass, "instrumentClass must not be null");
        this.handlerCode = Objects.requireNonNull(handlerCode, "handlerCode must not be null");
        this.expectedClass = Objects.requireNonNull(expectedClass, "expectedClass must not be null");
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
            HandlerCode handlerCode,
            Class<?> expectedClass
    ) {
        InstrumentCode ic = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Class<?> actual = Objects.requireNonNull(instrumentClass, "instrumentClass must not be null");
        HandlerCode hc = Objects.requireNonNull(handlerCode, "handlerCode must not be null");
        Class<?> expected = Objects.requireNonNull(expectedClass, "expectedClass must not be null");
        return "Instrument class mismatch (instrumentCode=" + ic.value() + ", actualClass=" + actual.getName()
                + ", handlerCode=" + hc.value() + ", expectedClass=" + expected.getName() + ")";
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
    public HandlerCode getHandlerCode() {
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
