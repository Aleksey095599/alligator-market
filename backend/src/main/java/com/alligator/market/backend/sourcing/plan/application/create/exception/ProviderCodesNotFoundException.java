package com.alligator.market.backend.sourcing.plan.application.create.exception;

import java.util.Collection;
import java.util.Objects;
import java.util.StringJoiner;

/* Исключение: один или несколько кодов провайдеров не найдены. */
public final class ProviderCodesNotFoundException extends IllegalArgumentException {

    public ProviderCodesNotFoundException(Collection<String> providerCodes) {
        super(buildMessage(providerCodes));
    }

    private static String buildMessage(Collection<String> providerCodes) {
        Objects.requireNonNull(providerCodes, "providerCodes must not be null");

        StringJoiner joiner = new StringJoiner(", ");
        for (String providerCode : providerCodes) {
            joiner.add(Objects.requireNonNull(providerCode, "providerCode must not be null"));
        }

        return "Provider codes do not exist: " + joiner;
    }
}
