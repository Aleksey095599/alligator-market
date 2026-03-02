package com.alligator.market.domain.instrument.support.forex.currency.exception;

import com.alligator.market.domain.common.exception.BaseDomainException;
import com.alligator.market.domain.common.exception.DomainErrorCode;

import java.util.Objects;

/**
 * Ошибка: дубликат валюты по имени.
 */
public final class CurrencyNameDuplicateException extends BaseDomainException {

    private final String name;

    /**
     * Создает исключение.
     *
     * @param name имя валюты
     */
    public CurrencyNameDuplicateException(String name) {
        super(DomainErrorCode.CURRENCY_NAME_DUPLICATE, msg(name));
        this.name = Objects.requireNonNull(name, "name must not be null");
    }

    /**
     * Создает исключение с причиной.
     *
     * @param name  имя валюты
     * @param cause причина ошибки
     */
    @SuppressWarnings("unused")
    public CurrencyNameDuplicateException(String name, Throwable cause) {
        super(DomainErrorCode.CURRENCY_NAME_DUPLICATE, msg(name), cause);
        this.name = Objects.requireNonNull(name, "name must not be null");
    }

    /**
     * Формирует сообщение об ошибке.
     *
     * @param name имя валюты
     * @return текст сообщения
     */
    private static String msg(String name) {
        String n = Objects.requireNonNull(name, "name must not be null");
        return "Currency with the same name already exists (name='" + n + "')";
    }

    /**
     * Возвращает имя валюты.
     *
     * @return имя валюты
     */
    @SuppressWarnings("unused")
    public String getName() {
        return name;
    }
}
