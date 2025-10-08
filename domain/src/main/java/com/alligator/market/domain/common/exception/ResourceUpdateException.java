package com.alligator.market.domain.common.exception;

/**
 * Базовое исключение для ошибок при обновлении ресурса.
 */
public class ResourceUpdateException extends RuntimeException {

    /* Имя ресурса (например, "Currency", "FxSpot"). */
    private final String resource;

    /* Причина, по которой не удалось обновить ресурс. */
    private final String reason;

    /** Конструктор с деталями. */
    public ResourceUpdateException(String resource, String reason) {
        super("Failed to update resource: " + resource +
                (reason == null || reason.isBlank() ? "" : " [" + reason + "]"));
        this.resource = resource;
        this.reason = reason;
    }

    /** Конструктор с первопричиной (если нужно прокинуть cause). */
    public ResourceUpdateException(String resource, String reason, Throwable cause) {
        super("Failed to update resource: " + resource +
                (reason == null || reason.isBlank() ? "" : " [" + reason + "]"), cause);
        this.resource = resource;
        this.reason = reason;
    }

    public String getResource() { return resource; }
    public String getReason() { return reason; }
}
