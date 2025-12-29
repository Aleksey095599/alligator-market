package com.alligator.market.domain.instrument.type.forex.spot.exception;

import com.alligator.market.domain.instrument.code.InstrumentCode;

import java.util.Objects;

/**
 * Ошибка удаления инструмента FX_SPOT.
 */
public final class FxSpotDeleteException extends RuntimeException {

    private final InstrumentCode instrumentCode;

    /**
     * Создает исключение.
     *
     * @param instrumentCode код инструмента
     */
    @SuppressWarnings("unused")
    public FxSpotDeleteException(InstrumentCode instrumentCode) {
        super(msg(instrumentCode));
        this.instrumentCode = instrumentCode;
    }

    /**
     * Создает исключение с причиной.
     *
     * @param instrumentCode код инструмента
     * @param cause          причина ошибки
     */
    public FxSpotDeleteException(InstrumentCode instrumentCode, Throwable cause) {
        super(msg(instrumentCode), cause);
        this.instrumentCode = instrumentCode;
    }

    /**
     * Формирует сообщение об ошибке.
     *
     * @param instrumentCode код инструмента
     * @return текст сообщения
     */
    private static String msg(InstrumentCode instrumentCode) {
        InstrumentCode code = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        return "Failed to delete FX_SPOT instrument (code=" + code.value() + ")";
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
