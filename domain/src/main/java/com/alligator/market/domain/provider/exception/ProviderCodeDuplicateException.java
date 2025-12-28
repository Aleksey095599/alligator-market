package com.alligator.market.domain.provider.exception;

import com.alligator.market.domain.provider.code.ProviderCode;

import java.util.Objects;

/**
 * Дублирование кодов провайдеров.
 */
public final class ProviderCodeDuplicateException extends RuntimeException {

    private final ProviderCode providerCode;

    /**
     * Создает исключение.
     *
     * @param providerCode код провайдера
     */
    public ProviderCodeDuplicateException(ProviderCode providerCode) {
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
    public ProviderCodeDuplicateException(ProviderCode providerCode, Throwable cause) {
        super(msg(providerCode), cause);
        this.providerCode = providerCode;
    }

    /**
     * Формирует сообщение об ошибке.
     *
     * @param providerCode код провайдера
     * @return текст сообщения
     */
    private static String msg(ProviderCode providerCode) {
        ProviderCode pc = Objects.requireNonNull(providerCode, "providerCode must not be null");
        return "Duplicate provider code detected (code=" + pc.value() + ")";
    }

    /**
     * Возвращает код провайдера.
     *
     * @return код провайдера
     */
    @SuppressWarnings("unused")
    public ProviderCode getProviderCode() {
        return providerCode;
    }
}
