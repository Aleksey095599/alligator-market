package com.alligator.market.backend.source.adapter.shared.poll;

import io.netty.channel.ConnectTimeoutException;
import io.netty.handler.timeout.ReadTimeoutException;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.net.SocketTimeoutException;
import java.util.Locale;
import java.util.Objects;

public enum SourcePollSkipReason {
    CONNECTION_TIMEOUT,
    RESPONSE_TIMEOUT,
    HTTP_ERROR,
    INVALID_RESPONSE,
    REQUEST_ERROR,
    UNKNOWN;

    public static SourcePollSkipReason from(Throwable error) {
        Objects.requireNonNull(error, "error must not be null");

        if (hasCause(error, ConnectTimeoutException.class) || messageContains(error, "connection timed out")) {
            return CONNECTION_TIMEOUT;
        }

        if (hasCause(error, ReadTimeoutException.class)
                || hasCause(error, SocketTimeoutException.class)
                || messageContains(error, "response timeout")
                || messageContains(error, "read timed out")) {
            return RESPONSE_TIMEOUT;
        }

        if (messageContains(error, "http error")) {
            return HTTP_ERROR;
        }

        if (error instanceof IllegalStateException) {
            return INVALID_RESPONSE;
        }

        if (error instanceof WebClientRequestException) {
            return REQUEST_ERROR;
        }

        return UNKNOWN;
    }

    private static boolean messageContains(Throwable error, String fragment) {
        String message = error.getMessage();
        if (message == null || message.isBlank()) {
            return false;
        }

        return message.toLowerCase(Locale.ROOT).contains(fragment);
    }

    private static boolean hasCause(Throwable error, Class<? extends Throwable> type) {
        Throwable current = error;
        while (current != null) {
            if (type.isInstance(current)) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }
}
