package com.alligator.market.domain.provider.exception;

import java.util.Objects;

/**
 * Инструмент не поддерживается обработчиком.
 */
public final class InstrumentNotSupportedException extends RuntimeException {

    private final String instrumentCode;
    private final String handlerCode;

    /**
     * Формирует сообщение об ошибке.
     *
     * @param instrumentCode код инструмента, который не поддерживается
     * @param handlerCode    код обработчика, который не поддерживает {@code instrumentCode}
     * @return текст сообщения
     */
    private static String msg(String instrumentCode, String handlerCode) {
        String ic = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        String hc = Objects.requireNonNull(handlerCode, "handlerCode must not be null");
        return "Instrument not supported (instrumentCode=" + ic + ", handlerCode=" + hc + ")";
    }

    /**
     * Создает исключение.
     *
     * @param instrumentCode код инструмента, который не поддерживается
     * @param handlerCode    код обработчика, который не поддерживает {@code instrumentCode}
     */
    @SuppressWarnings("unused")
    public InstrumentNotSupportedException(String instrumentCode, String handlerCode) {
        super(msg(instrumentCode, handlerCode));
        this.instrumentCode = instrumentCode;
        this.handlerCode = handlerCode;
    }

    /**
     * Создает исключение с причиной.
     *
     * @param instrumentCode код инструмента, который не поддерживается
     * @param handlerCode    код обработчика, который не поддерживает {@code instrumentCode}
     * @param cause причина ошибки
     */
    @SuppressWarnings("unused")
    public InstrumentNotSupportedException(String instrumentCode, String handlerCode, Throwable cause) {
        super(msg(instrumentCode, handlerCode), cause);
        this.instrumentCode = instrumentCode;
        this.handlerCode = handlerCode;
    }

    /**
     * Возвращает код инструмента.
     *
     * @return код инструмента
     */
    @SuppressWarnings("unused")
    public String getInstrumentCode() { return instrumentCode; }

    /**
     * Возвращает код обработчика.
     *
     * @return код обработчика
     */
    @SuppressWarnings("unused")
    public String getHandlerCode() { return handlerCode; }
}
