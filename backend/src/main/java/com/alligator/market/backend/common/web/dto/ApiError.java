package com.alligator.market.backend.common.web.dto;

import jakarta.validation.ConstraintViolation;
import org.springframework.validation.FieldError;

/* DTO-обёртка для сведений об одной ошибке (совместимо с RFC 7807). */
public record ApiError(
        String code,    // машинный идентификатор проблемы (например, ERR-001)
        String detail,  // человеко-читаемое описание
        String field    // имя поля (для ошибок валидации) или null
) {

    /* ---------- Статические фабрики ---------- */
    // Ошибка валидации (тело @Valid)
    public static ApiError validation(FieldError fe) {
        return new ApiError("VALIDATION", fe.getDefaultMessage(), fe.getField());
    }

    // Ошибка валидации (query/path @Validated)
    public static ApiError validation(ConstraintViolation<?> v) {
        return new ApiError("VALIDATION", v.getMessage(), v.getPropertyPath().toString());
    }

    // Ресурс не найден
    public static ApiError notFound(String message) {
        return new ApiError("NOT_FOUND", message, null);
    }

    // Непредвиденная внутренняя ошибка
    public static ApiError unexpected() {
        return new ApiError("UNEXPECTED", "Internal server error", null);
    }

}
