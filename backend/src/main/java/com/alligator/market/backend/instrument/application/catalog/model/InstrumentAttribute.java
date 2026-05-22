package com.alligator.market.backend.instrument.application.catalog.model;

import java.util.Objects;

public record InstrumentAttribute(
        String key,
        String label,
        String value
) {
    public InstrumentAttribute {
        key = requireText(key, "key");
        label = requireText(label, "label");
        value = requireText(value, "value");
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " must not be null");

        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }

        return value;
    }
}
