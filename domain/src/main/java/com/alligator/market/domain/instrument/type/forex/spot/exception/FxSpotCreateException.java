package com.alligator.market.domain.instrument.type.forex.spot.exception;

import java.util.Objects;

/**
 * Ошибка создания инструмента FX_SPOT.
 */
public final class FxSpotCreateException extends RuntimeException {

    private final String instrumentCode;

    /**
     * Создает исключение.
     *
     * @param instrumentCode код инструмента
     */
    @SuppressWarnings("unused")
    public FxSpotCreateException(String instrumentCode) {
        super(msg(instrumentCode));
        this.instrumentCode = instrumentCode;
    }

    /**
     * Создает исключение с причиной.
     *
     * @param instrumentCode код инструмента
     * @param cause          причина ошибки
     */
    public FxSpotCreateException(String instrumentCode, Throwable cause) {
        super(msg(instrumentCode), cause);
        this.instrumentCode = instrumentCode;
    }

    /**
     * Формирует сообщение об ошибке.
     *
     * @param instrumentCode код инструмента
     * @return текст сообщения
     */
    private static String msg(String instrumentCode) {
        String code = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        return "Failed to create FX_SPOT instrument (code=" + code + ")";
    }

    /**
     * Возвращает код инструмента.
     *
     * @return код инструмента
     */
    public String getInstrumentCode() {
        return instrumentCode;
    }
}
