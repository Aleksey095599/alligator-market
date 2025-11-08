package com.alligator.market.domain.provider.exception;

import java.util.Objects;

/**
 * Дублирование кодов провайдеров.
 */
public final class ProviderCodeDuplicateException extends RuntimeException {

    private final String providerCode;

    /**
     * Создает исключение.
     *
     * @param providerCode код провайдера
     */
    public ProviderCodeDuplicateException(String providerCode) {
        super(msg(providerCode));
        this.providerCode = providerCode;
    }

    /**
     * Создает исключение с причиной.
     *
     * @param providerCode код провайдера
     * @param cause        причина ошибки
     */
    @SuppressWarnings("unused")
    public ProviderCodeDuplicateException(String providerCode, Throwable cause) {
        super(msg(providerCode), cause);
        this.providerCode = providerCode;
    }

    /**
     * Формирует сообщение об ошибке.
     *
     * @param providerCode код провайдера
     * @return текст сообщения
     */
    private static String msg(String providerCode) {
        String pc = Objects.requireNonNull(providerCode, "providerCode must not be null");
        return "Duplicate provider code detected (code=" + pc + ")";
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
