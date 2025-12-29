package com.alligator.market.domain.instrument.type.forex.spot.exception;

import com.alligator.market.domain.instrument.code.InstrumentCode;

import java.util.Objects;

/**
 * Ошибка повторного создания инструмента FX_SPOT.
 */
public final class FxSpotAlreadyExistsException extends RuntimeException {

    private final InstrumentCode instrumentCode;

    /**
     * Создает исключение.
     *
     * @param instrumentCode код инструмента
     */
    public FxSpotAlreadyExistsException(InstrumentCode instrumentCode) {
        super(msg(instrumentCode));
        this.instrumentCode = instrumentCode;
    }

    /**
     * Создает исключение с причиной.
     *
     * @param instrumentCode код инструмента
     * @param cause          причина ошибки
     */
    @SuppressWarnings("unused")
    public FxSpotAlreadyExistsException(InstrumentCode instrumentCode, Throwable cause) {
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
        return "FX_SPOT instrument already exists (code=" + code.value() + ")";
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
