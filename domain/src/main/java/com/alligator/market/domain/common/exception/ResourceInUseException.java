package com.alligator.market.domain.common.exception;

/**
 * Базовое исключение для случаев, когда ресурс задействован в другом контексте.
 */
public class ResourceInUseException extends RuntimeException {

    /* Имя ресурса (например, "Currency", "FxSpot"). */
    private final String resource;

    /* Контекст применения ресурса (например, "Currency" используется в инструменте "FxSpot"). */
    private final String usageContext;

    /** Конструктор с деталями. */
    public ResourceInUseException(String resource, String usageContext) {
        super("Resource in use: " + resource +
                (usageContext == null || usageContext.isBlank() ? "" : " [" + usageContext + "]"));
        this.resource = resource;
        this.usageContext = usageContext;
    }

    /** Конструктор с первопричиной (если нужно прокинуть cause). */
    public ResourceInUseException(String resource, String usageContext, Throwable cause) {
        super("Resource in use: " + resource +
                (usageContext == null || usageContext.isBlank() ? "" : " [" + usageContext + "]"), cause);
        this.resource = resource;
        this.usageContext = usageContext;
    }

    public String getResource() { return resource; }
    public String getUsageContext() { return usageContext; }
}
