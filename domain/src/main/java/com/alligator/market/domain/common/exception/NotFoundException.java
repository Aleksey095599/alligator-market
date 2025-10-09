package com.alligator.market.domain.common.exception;

import java.util.Objects;

/**
 * Базовое исключение для случаев, когда ресурс не найден.
 */
public class NotFoundException extends RuntimeException {

    /* Имя ресурса (например, "Currency" или "FxSpot"). */
    private final String resource;

    /* Критерий поиска (например, код валюты). */
    private final String criteria;

    /* Сообщение. */
    private static String msg(String resource, String criteria) {
        Objects.requireNonNull(resource, "resource must not be null");
        if (criteria == null || criteria.isBlank()) {
            return "Resource not found: " + resource;
        }
        return "Resource not found: " + resource + " (by criteria: " + criteria + ")";
    }

    /** Конструктор с деталями. */
    public NotFoundException(String resource, String criteria) {
        super(msg(resource, criteria));
        this.resource = resource;
        this.criteria = criteria;
    }

    /** Конструктор с первопричиной (если нужно прокинуть cause). */
    public NotFoundException(String resource, String criteria, Throwable cause) {
        super(msg(resource, criteria), cause);
        this.resource = resource;
        this.criteria = criteria;
    }

    public String getResource() { return resource; }
    public String getCriteria() { return criteria; }
}
