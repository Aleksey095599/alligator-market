package com.alligator.market.domain.common.exception;

/**
 * Сущность уже существует.
 */
public class DuplicateEntityException extends RuntimeException {

    private final String entityName;
    private final String field;
    private final String value;

    public DuplicateEntityException(String entityName, String field, String value) {
        super("%s with %s '%s' already exists".formatted(entityName, field, value));
        this.entityName = entityName;
        this.field = field;
        this.value = value;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }
}
