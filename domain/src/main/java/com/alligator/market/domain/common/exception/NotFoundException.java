package com.alligator.market.domain.common.exception;

/** Базовое исключение для случаев, когда ресурс не найден. */
public class NotFoundException extends RuntimeException {

    /* Имя ресурса (например, "Currency", "CurrencyPair"). */
    private final String resource;

    /* Селектор/ключ поиска (например, код валюты или код валютной пары). */
    private final String selector;

    /** Конструктор с деталями. */
    public NotFoundException(String resource, String selector) {
        super("Resource not found: " + resource +
                (selector == null || selector.isBlank() ? "" : " [" + selector + "]"));
        this.resource = resource;
        this.selector = selector;
    }

    /** Конструктор с первопричиной (если нужно прокинуть cause). */
    public NotFoundException(String resource, String selector, Throwable cause) {
        super("Resource not found: " + resource +
                (selector == null || selector.isBlank() ? "" : " [" + selector + "]"), cause);
        this.resource = resource;
        this.selector = selector;
    }

    public String resource() { return resource; }
    public String selector() { return selector; }

    /* ↓↓ Удобные фабрики — чтобы не плодить строки в коде. */

    /** Например: NotFoundException.currency(code). */
    public static NotFoundException currency(String code) {
        return new NotFoundException("Currency", "code=" + code);
    }

    /** Например: NotFoundException.currencyPair(base, quote). */
    public static NotFoundException currencyPair(String baseCode, String quoteCode) {
        return new NotFoundException("CurrencyPair", "baseCode=" + baseCode + ", quoteCode=" + quoteCode);
    }

    /** Общая фабрика по типу класса. */
    public static NotFoundException of(Class<?> type, String selector) {
        return new NotFoundException(type.getSimpleName(), selector);
    }
}
