package com.alligator.market.backend.common.web.http;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

/**
 * Унифицированный конверт REST-ответов.
 */
@JsonInclude(JsonInclude.Include.NON_NULL) // <-- Не включать в JSON свойства со значением null (общее правило)
public record ApiResponse<T>(
        @JsonInclude(JsonInclude.Include.ALWAYS) // <-- Для поля ниже исключение — всегда включаем в JSON, даже если null
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
