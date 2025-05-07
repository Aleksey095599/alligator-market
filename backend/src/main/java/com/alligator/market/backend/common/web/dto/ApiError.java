package com.alligator.market.backend.common.web.dto;

/* Детали одной конкретной ошибки (совместимо с RFC 7807). */
public record ApiError(
        String code,    // machine-readable error code (например, ERR-001)
        String detail,  // человеко-читаемое описание
        String field    // связанное поле запроса; null, если ошибка общая
) {
}
