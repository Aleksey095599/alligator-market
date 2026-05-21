package com.alligator.market.backend.source.adapter.shared.poll;

import org.springframework.http.HttpStatusCode;

import java.util.Objects;

public final class SourcePollHttpException extends RuntimeException {
    private final HttpStatusCode statusCode;
    private final String responseBody;

    public SourcePollHttpException(
            HttpStatusCode statusCode,
            String responseBody,
            String context
    ) {
        super(message(statusCode, context));
        this.statusCode = Objects.requireNonNull(statusCode, "statusCode must not be null");
        this.responseBody = Objects.requireNonNullElse(responseBody, "");
    }

    public HttpStatusCode statusCode() {
        return statusCode;
    }

    public String responseBody() {
        return responseBody;
    }

    private static String message(HttpStatusCode statusCode, String context) {
        Objects.requireNonNull(statusCode, "statusCode must not be null");

        if (context == null || context.isBlank()) {
            return "Source poll HTTP error " + statusCode.value();
        }

        return "Source poll HTTP error " + statusCode.value() + ": " + context;
    }
}
