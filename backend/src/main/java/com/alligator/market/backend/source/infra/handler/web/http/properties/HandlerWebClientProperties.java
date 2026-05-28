package com.alligator.market.backend.source.infra.handler.web.http.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.util.Objects;

@Validated
@ConfigurationProperties("market-data-source.handler-web-client")
public record HandlerWebClientProperties(
        Duration connectTimeout,
        Duration responseTimeout
) {

    public HandlerWebClientProperties {
        requirePositiveMillis(connectTimeout, "connectTimeout");
        requirePositiveMillis(responseTimeout, "responseTimeout");

        if (connectTimeout.toMillis() > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("connectTimeout must not exceed " + Integer.MAX_VALUE + " ms");
        }
    }

    public int connectTimeoutMillis() {
        return Math.toIntExact(connectTimeout.toMillis());
    }

    private static void requirePositiveMillis(Duration value, String name) {
        Objects.requireNonNull(value, name + " must not be null");

        if (value.toMillis() <= 0) {
            throw new IllegalArgumentException(name + " must be positive and at least 1 ms");
        }
    }
}
