package com.alligator.market.domain.instrument.type.forex.spot.exception;

import java.util.Objects;

/**
 * Ошибка повторного создания инструмента FX_SPOT.
 */
public final class FxSpotAlreadyExistsException extends RuntimeException {

    private final String instrumentCode;

    /**
     * Формирует сообщение об ошибке.
     *
     * @param instrumentCode код инструмента
     * @return текст сообщения
     */
    private static String msg(String instrumentCode) {
        String code = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        return "FX_SPOT instrument already exists (code=" + code + ")";
    }

    /**
     * Создает исключение.
     *
     * @param instrumentCode код инструмента
     */
    @SuppressWarnings("unused")
    public FxSpotAlreadyExistsException(String instrumentCode) {
        super(msg(instrumentCode));
        this.instrumentCode = instrumentCode;
    }

    /**
     * Создает исключение с причиной.
     *
     * @param instrumentCode код инструмента
     * @param cause причина ошибки
     */
    @SuppressWarnings("unused")
    public FxSpotAlreadyExistsException(String instrumentCode, Throwable cause) {
        super(msg(instrumentCode), cause);
        this.instrumentCode = instrumentCode;
    }

    /**
     * Возвращает код инструмента.
     *
     * @return код инструмента
     */
    public String getInstrumentCode() { return instrumentCode; }
}
