package com.alligator.market.domain.provider.exception;

import java.util.Objects;

/**
 * Обработчик для инструмента не найден.
 */
public final class HandlerNotFoundException extends RuntimeException {

    private final String instrumentCode;
    private final String providerCode;

    /**
     * Создает исключение.
     *
     * @param instrumentCode код инструмента
     * @param providerCode   код провайдера
     */
    public HandlerNotFoundException(String instrumentCode, String providerCode) {
        super(msg(instrumentCode, providerCode));
        this.instrumentCode = instrumentCode;
        this.providerCode = providerCode;
    }

    /**
     * Создает исключение с причиной.
     *
     * @param instrumentCode код инструмента
     * @param providerCode   код провайдера
     * @param cause          причина ошибки
     */
    @SuppressWarnings("unused")
    public HandlerNotFoundException(String instrumentCode, String providerCode, Throwable cause) {
        super(msg(instrumentCode, providerCode), cause);
        this.instrumentCode = instrumentCode;
        this.providerCode = providerCode;
    }

    /**
     * Формирует сообщение об ошибке.
     *
     * @param instrumentCode код инструмента
     * @param providerCode   код провайдера
     * @return текст сообщения
     */
    private static String msg(String instrumentCode, String providerCode) {
        String ic = Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        String pc = Objects.requireNonNull(providerCode, "providerCode must not be null");
        return "Handler not found (instrumentCode=" + ic + ", providerCode=" + pc + ")";
    }

    /**
     * Возвращает код инструмента.
     *
     * @return код инструмента
     */
    public String getInstrumentCode() {
        return instrumentCode;
    }

    /**
     * Возвращает код провайдера.
     *
     * @return код провайдера
     */
    public String getProviderCode() {
        return providerCode;
    }
}
