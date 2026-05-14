package com.alligator.market.domain.shared.code;

import java.util.Objects;
import java.util.regex.Pattern;

public final class DomainCodeFormat {
    public static final String REGEX = "^[A-Z0-9_]+$";
    public static final String DESCRIPTION = "[A-Z0-9_]+";

    private static final Pattern PATTERN = Pattern.compile(REGEX);

    private DomainCodeFormat() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void requireValidEnumCode(String enumTypeName, String code) {
        Objects.requireNonNull(enumTypeName, "enumTypeName must not be null");
        Objects.requireNonNull(code, "code must not be null");

        if (enumTypeName.isBlank()) {
            throw new IllegalArgumentException("enumTypeName must not be blank");
        }

        if (!PATTERN.matcher(code).matches()) {
            throw new IllegalStateException(
                    enumTypeName + " code must match pattern " + DESCRIPTION + ": " + code
            );
        }
    }
}
