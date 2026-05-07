package com.alligator.market.backend.sourceplan.plan.application.exception;

import java.util.Collection;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Ошибка application-слоя: один или несколько кодов провайдеров не найдены.
 */
public final class MarketDataSourceCodesNotFoundException extends IllegalArgumentException {

    public MarketDataSourceCodesNotFoundException(Collection<String> sourceCodes) {
        super(buildMessage(sourceCodes));
    }

    private static String buildMessage(Collection<String> sourceCodes) {
        Objects.requireNonNull(sourceCodes, "sourceCodes must not be null");

        StringJoiner joiner = new StringJoiner(", ");
        for (String sourceCode : sourceCodes) {
            joiner.add(Objects.requireNonNull(sourceCode, "sourceCode must not be null"));
        }

        return "Market data source codes do not exist: " + joiner;
    }
}
