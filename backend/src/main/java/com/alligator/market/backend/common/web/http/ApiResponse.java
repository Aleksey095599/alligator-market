package com.alligator.market.backend.common.web.http;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

/**
 * Унифицированный конверт REST-ответов.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        T data,
        boolean success,
        String errorCode,
        String message,
        Instant timestamp
) {

    /** Фабрика успешного ответа. */
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(data, true, null, "success", Instant.now());
    }

    /** Фабрика ошибочного ответа. */
    public static ApiResponse<Void> error(String code, String message) {
        return new ApiResponse<>(null, false, code, message, Instant.now());
    }
}
