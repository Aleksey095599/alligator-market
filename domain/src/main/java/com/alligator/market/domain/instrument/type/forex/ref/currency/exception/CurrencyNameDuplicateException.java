package com.alligator.market.domain.instrument.type.forex.ref.currency.exception;

import java.util.Objects;

/**
 * Ошибка: дубликат валюты по имени.
 */
public final class CurrencyNameDuplicateException extends RuntimeException {

    private final String name;

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
     * Создает исключение.
     *
     * @param name имя валюты
     */
    @SuppressWarnings("unused")
    public CurrencyNameDuplicateException(String name) {
        super(msg(name));
        this.name = name;
    }

    /**
     * Создает исключение с причиной.
     *
     * @param name  имя валюты
     * @param cause причина ошибки
     */
    @SuppressWarnings("unused")
    public CurrencyNameDuplicateException(String name, Throwable cause) {
        super(msg(name), cause);
        this.name = name;
    }

    /**
     * Возвращает имя валюты.
     *
     * @return имя валюты
     */
    public String getName() { return name; }
}
