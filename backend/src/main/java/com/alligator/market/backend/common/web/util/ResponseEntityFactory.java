package com.alligator.market.backend.common.web.util;

import com.alligator.market.backend.common.web.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;

/* Фабрика готовых ResponseEntity<ApiResponse<?>>.
   Позволяет легко создавать единообразные HTTP-ответы, содержащие унифицированный конверт ApiResponse<?>.
   Позволяет централизованно добавлять новые параметры в HTTP-ответ. */
public final class ResponseEntityFactory {

    private ResponseEntityFactory() {
        // utility class – no instances
    }

    /* 200 OK */
    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ResponseEntity.ok(ApiResponse.build(data, "success"));
    }

    /* 201 Created + Location */
    public static <T> ResponseEntity<ApiResponse<T>> created(URI location, T data) {
        return ResponseEntity.created(location)
                .body(ApiResponse.build(data, "created"));
    }

    /* 201 Created + Location (без body) */
    public static ResponseEntity<ApiResponse<Void>> created(URI location) {
        return ResponseEntity.created(location)
                .build();
    }

    /* Стандартная ошибка */
    public static ResponseEntity<ApiResponse<Void>> error(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(ApiResponse.build(null, message));
    }

    /* Ошибка 404 Not Found */
    public static ResponseEntity<ApiResponse<Void>> notFound(String message) {
        return error(HttpStatus.NOT_FOUND, message);
    }
}
