package com.alligator.market.domain.provider.exception;

import java.util.Objects;

/**
 * Дублирование отображаемых имен провайдеров.
 */
public final class ProviderDisplayNameDuplicateException extends RuntimeException {

    private final String displayName;

    /**
     * Создает исключение.
     *
     * @param displayName отображаемое имя провайдера
     */
    public ProviderDisplayNameDuplicateException(String displayName) {
        super(msg(displayName));
        this.displayName = displayName;
    }

    /**
     * Создает исключение с причиной.
     *
     * @param displayName отображаемое имя провайдера
     * @param cause       причина ошибки
     */
    @SuppressWarnings("unused")
    public ProviderDisplayNameDuplicateException(String displayName, Throwable cause) {
        super(msg(displayName), cause);
        this.displayName = displayName;
    }

    /**
     * Формирует сообщение об ошибке.
     *
     * @param displayName отображаемое имя провайдера
     * @return текст сообщения
     */
    private static String msg(String displayName) {
        String dn = Objects.requireNonNull(displayName, "displayName must not be null");
        return "Duplicate provider display name detected (displayName=" + dn + ")";
    }

    /**
     * Возвращает отображаемое имя провайдера.
     *
     * @return отображаемое имя провайдера
     */
    @SuppressWarnings("unused")
    public String getDisplayName() {
        return displayName;
    }
}
