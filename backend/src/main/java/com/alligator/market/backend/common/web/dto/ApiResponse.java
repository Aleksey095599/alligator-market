package com.alligator.market.backend.common.web.dto;

import java.time.Instant;

/* Простой унифицированный конверт для любых API REST ответов.
   Допускает расширение. */
public record ApiResponse<T>(
        T data,
        String message,
        Instant timestamp
) {
    /* Статическая «фабрика» для создания конверта. */
    public static <T> ApiResponse<T> build(T data, String message) {
        return new ApiResponse<>(data, message, Instant.now());
    }
}
