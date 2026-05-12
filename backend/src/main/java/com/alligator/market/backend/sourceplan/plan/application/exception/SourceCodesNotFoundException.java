package com.alligator.market.backend.sourceplan.plan.application.exception;

import java.util.Collection;
import java.util.Objects;
import java.util.StringJoiner;

public final class SourceCodesNotFoundException extends IllegalArgumentException {
    public SourceCodesNotFoundException(Collection<String> sourceCodes) {
        super(buildMessage(sourceCodes));
    }

    private static String buildMessage(Collection<String> sourceCodes) {
        Objects.requireNonNull(sourceCodes, "sourceCodes must not be null");

        StringJoiner joiner = new StringJoiner(", ");
        for (String sourceCode : sourceCodes) {
            joiner.add(Objects.requireNonNull(sourceCode, "sourceCode must not be null"));
        }

        return "Market source codes do not exist: " + joiner;
    }
}
