package com.alligator.market.domain.instrument.type.forex.spot.exception;

import java.util.Objects;

/**
 * Инструмент FxSpot не найден.
 */
public final class FxSpotNotFoundException extends RuntimeException {

    private final String code;

    /**
     * Формирует сообщение об ошибке.
     *
     * @param instrumentCode код инструмента
     * @return текст сообщения
     */
    private static String msg(String instrumentCode) {
        String c = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        return "FxSpot instrument not found (code=" + c + ")";
    }

    /**
     * Создает исключение.
     *
     * @param instrumentCode код инструмента
     */
    @SuppressWarnings("unused")
    public FxSpotNotFoundException(String instrumentCode) {
        super(msg(instrumentCode));
        this.code = instrumentCode;
    }

    /**
     * Создает исключение с причиной.
     *
     * @param instrumentCode код инструмента
     * @param cause причина ошибки
     */
    @SuppressWarnings("unused")
    public FxSpotNotFoundException(String instrumentCode, Throwable cause) {
        super(msg(instrumentCode), cause);
        this.code = instrumentCode;
    }

    /**
     * Возвращает код инструмента.
     *
     * @return код инструмента
     */
    public String getCode() { return code; }
}
