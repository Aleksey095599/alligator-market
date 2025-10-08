package com.alligator.market.domain.common.exception;

/**
 * Базовое исключение для ошибок при создании ресурса.
 */
public class ResourceCreationException extends RuntimeException {

    /* Имя ресурса (например, "Currency", "FxSpot"). */
    private final String resource;

    /* Причина, по которой не удалось создать ресурс. */
    private final String reason;

    /** Конструктор с деталями. */
    public ResourceCreationException(String resource, String reason) {
        super("Failed to create resource: " + resource +
                (reason == null || reason.isBlank() ? "" : " [" + reason + "]"));
        this.resource = resource;
        this.reason = reason;
    }

    /** Конструктор с первопричиной (если нужно прокинуть cause). */
    public ResourceCreationException(String resource, String reason, Throwable cause) {
        super("Failed to create resource: " + resource +
                (reason == null || reason.isBlank() ? "" : " [" + reason + "]"), cause);
        this.resource = resource;
        this.reason = reason;
    }

    public String getResource() { return resource; }
    public String getReason() { return reason; }
}
