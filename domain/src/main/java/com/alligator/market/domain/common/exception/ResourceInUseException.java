package com.alligator.market.domain.common.exception;

/**
 * Ресурс используется в другом контексте.
 */
public class ResourceInUseException extends RuntimeException {

    /* Имя ресурса (например, "Currency", "FxSpot"). */
    private final String resource;

    /* Контекст применения ресурса (например, валюта используется в инструменте FX_SPOT). */
    private final String usageContext;

    /** Конструктор с деталями. */
    public ResourceInUseException(String resource, String usageContext) {
        super("Resource %s is used in %s".formatted(resource, usageContext));
        this.resource = resource;
        this.usageContext = usageContext;
    }

    public String getResource() {
        return resource;
    }

    public String getUsageContext() {
        return usageContext;
    }
}
