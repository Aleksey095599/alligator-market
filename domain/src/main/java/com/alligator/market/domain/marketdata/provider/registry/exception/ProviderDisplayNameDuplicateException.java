package com.alligator.market.domain.marketdata.provider.registry.exception;

import com.alligator.market.domain.common.exception.BaseDomainException;
import com.alligator.market.domain.common.exception.DomainErrorCode;

import java.util.Objects;

/**
 * Дублирование отображаемых имен провайдеров.
 */
public final class ProviderDisplayNameDuplicateException extends BaseDomainException {

    private final String displayName;

    /**
     * Создает исключение.
     *
     * @param displayName отображаемое имя провайдера
     */
    public ProviderDisplayNameDuplicateException(String displayName) {
        super(DomainErrorCode.PROVIDER_DISPLAY_NAME_DUPLICATE, msg(displayName));
        this.displayName = Objects.requireNonNull(displayName, "displayName must not be null");
    }

    /**
     * Создает исключение с причиной.
     *
     * @param displayName отображаемое имя провайдера
     * @param cause       причина ошибки
     */
    @SuppressWarnings("unused")
    public ProviderDisplayNameDuplicateException(String displayName, Throwable cause) {
        super(DomainErrorCode.PROVIDER_DISPLAY_NAME_DUPLICATE, msg(displayName), cause);
        this.displayName = Objects.requireNonNull(displayName, "displayName must not be null");
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
