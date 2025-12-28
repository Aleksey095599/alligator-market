package com.alligator.market.domain.instrument.type.forex.spot.exception;

import com.alligator.market.domain.instrument.code.InstrumentCode;

import java.util.Objects;

/**
 * Ошибка обновления инструмента FX_SPOT.
 */
public final class FxSpotUpdateException extends RuntimeException {

    private final InstrumentCode instrumentCode;

    /**
     * Создает исключение.
     *
     * @param instrumentCode код инструмента
     */
    @SuppressWarnings("unused")
    public FxSpotUpdateException(InstrumentCode instrumentCode) {
        super(msg(instrumentCode));
        this.instrumentCode = instrumentCode;
    }

    /**
     * Создает исключение.
     *
     * @param instrumentCode код инструмента
     */
    @SuppressWarnings("unused")
    public FxSpotUpdateException(String instrumentCode) {
        this(InstrumentCode.of(instrumentCode));
    }

    /**
     * Создает исключение с причиной.
     *
     * @param instrumentCode код инструмента
     * @param cause          причина ошибки
     */
    public FxSpotUpdateException(InstrumentCode instrumentCode, Throwable cause) {
        super(msg(instrumentCode), cause);
        this.instrumentCode = instrumentCode;
    }

    /**
     * Создает исключение с причиной.
     *
     * @param instrumentCode код инструмента
     * @param cause          причина ошибки
     */
    public FxSpotUpdateException(String instrumentCode, Throwable cause) {
        this(InstrumentCode.of(instrumentCode), cause);
    }

    /**
     * Формирует сообщение об ошибке.
     *
     * @param instrumentCode код инструмента
     * @return текст сообщения
     */
    private static String msg(InstrumentCode instrumentCode) {
        InstrumentCode code = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        return "Failed to update FX_SPOT instrument (code=" + code.value() + ")";
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
