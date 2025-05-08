package com.alligator.market.backend.common.web.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/* Унифицированный ответ сервера. */
public record ApiResponse<T>(
        boolean success,          // признак успешного выполнения запроса
        T data,                   // полезная нагрузка
        String message,           // краткое человеко-читаемое сообщение
        List<ApiError> errors,    // список ошибок
        Map<String, Object> meta, // произвольная служебная информация
        LocalDateTime timestamp,  // момент формирования ответа
        String path               // оригинальный URI запроса
) {
    /* Быстрый фабричный метод для успешного ответа. */
    public static <T> ApiResponse<T> ok(T data) {

        return new ApiResponse<>(
                true,
                data,
                "OK",
                null,
                null,
                LocalDateTime.now(),
                null
        );
    }

    /* Быстрый фабричный метод для ошибки. */
    public static ApiResponse<Void> fail(String message, List<ApiError> errors, String path) {

        return new ApiResponse<>(
                false,
                null,
                message,
                errors,
                null,
                LocalDateTime.now(),
                path);
    }
}
