package com.alligator.market.domain.instrument.type.forex.ref.currency.exception;

import java.util.Objects;

/**
 * Ошибка: валюта уже существует (дубликат).
 */
public final class CurrencyDuplicateException extends RuntimeException {

    // Поле-конфликт: "code" или "name"
    private final String field;
    // Значение поля, например "USD" или "US Dollar"
    private final String value;

    public CurrencyDuplicateException(String field, String value) {
        super("Currency already exists (" +
                (Objects.requireNonNull(field, "field must not be null")) + "='" +
                (Objects.requireNonNull(value, "value must not be null")) + "')");
        this.field = field;
        this.value = value;
    }

    public CurrencyDuplicateException(String field, String value, Throwable cause) {
        super("Currency already exists (" + field + "='" + value + "')", cause);
        this.field = Objects.requireNonNull(field, "field must not be null");
        this.value = Objects.requireNonNull(value, "value must not be null");
    }

    /** Сахар: дубликат по коду. */
    public static CurrencyDuplicateException byCode(String code) {
        return new CurrencyDuplicateException("code", code);
    }

    /** Сахар: дубликат по имени. */
    public static CurrencyDuplicateException byName(String name) {
        return new CurrencyDuplicateException("name", name);
    }

    public String getField() { return field; }
    public String getValue() { return value; }
}
