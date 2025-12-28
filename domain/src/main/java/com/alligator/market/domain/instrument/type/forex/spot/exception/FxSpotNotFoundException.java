package com.alligator.market.domain.instrument.type.forex.spot.exception;

import com.alligator.market.domain.instrument.code.InstrumentCode;

import java.util.Objects;

/**
 * Ошибка поиска инструмента FX_SPOT.
 */
public final class FxSpotNotFoundException extends RuntimeException {

    private final InstrumentCode instrumentCode;

    /**
     * Создает исключение.
     *
     * @param instrumentCode код инструмента
     */
    public FxSpotNotFoundException(InstrumentCode instrumentCode) {
        super(msg(instrumentCode));
        this.instrumentCode = instrumentCode;
    }

    /**
     * Создает исключение.
     *
     * @param instrumentCode код инструмента
     */
    public FxSpotNotFoundException(String instrumentCode) {
        this(InstrumentCode.of(instrumentCode));
    }

    /**
     * Создает исключение с причиной.
     *
     * @param instrumentCode код инструмента
     * @param cause          причина ошибки
     */
    @SuppressWarnings("unused")
    public FxSpotNotFoundException(InstrumentCode instrumentCode, Throwable cause) {
        super(msg(instrumentCode), cause);
        this.instrumentCode = instrumentCode;
    }

    /**
     * Создает исключение с причиной.
     *
     * @param instrumentCode код инструмента
     * @param cause          причина ошибки
     */
    public FxSpotNotFoundException(String instrumentCode, Throwable cause) {
        this(InstrumentCode.of(instrumentCode), cause);
    }

    /**
     * Формирует сообщение об ошибке.
     *
     * @param instrumentCode код инструмента
     * @return текст сообщения
     */
    private static String msg(InstrumentCode instrumentCode) {
        InstrumentCode c = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        return "FX_SPOT instrument not found (code=" + c.value() + ")";
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
}
