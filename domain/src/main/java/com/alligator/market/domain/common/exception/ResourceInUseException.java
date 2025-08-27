package com.alligator.market.domain.common.exception;

/**
 * Ресурс используется в другом контексте.
 */
public class ResourceInUseException extends RuntimeException {
    private final String resource;
    private final String usageContext;

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
