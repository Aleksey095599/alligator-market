package com.alligator.market.domain.instrument.type.forex.spot.exception;

import java.util.Objects;

/**
 * Ошибка удаления инструмента FX_SPOT.
 */
public final class FxSpotDeleteException extends RuntimeException {

    private final String instrumentCode;

    /**
     * Формирует сообщение об ошибке.
     *
     * @param instrumentCode код инструмента
     * @return текст сообщения
     */
    private static String msg(String instrumentCode) {
        String code = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        return "Failed to delete FX_SPOT instrument (code=" + code + ")";
    }

    /**
     * Создает исключение.
     *
     * @param instrumentCode код инструмента
     */
    @SuppressWarnings("unused")
    public FxSpotDeleteException(String instrumentCode) {
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
    public FxSpotDeleteException(String instrumentCode, Throwable cause) {
        super(msg(instrumentCode), cause);
        this.instrumentCode = instrumentCode;
    }

    /**
     * Возвращает код инструмента.
     *
     * @return код инструмента
     */
    @SuppressWarnings("unused")
    public String getInstrumentCode() { return instrumentCode; }
}
