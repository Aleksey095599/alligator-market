package com.alligator.market.backend.common.web.http;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;

/**
 * Фабрика готовых унифицированных HTTP-ответов, использующих конверт {@link ApiResponse}.
 */
public final class ResponseEntityFactory {

    private ResponseEntityFactory() {}

    /** Успех: 200 OK. */
    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ResponseEntity.ok(ApiResponse.build(data, "success"));
    }

    /** Успех: частный случай - 201 Created + Location. */
    public static <T> ResponseEntity<ApiResponse<T>> created(URI location, T data) {
        return ResponseEntity.created(location)
                .body(ApiResponse.build(data, "created"));
    }

    /**
     * Успех: частный случай - 204 No Content.
     * Возвращаем ответ без конверта, потому что тело должно отсутствовать.
     */
    public static ResponseEntity<Void> noContent() {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /** Ошибка: общая. */
    public static ResponseEntity<ApiResponse<Void>> error(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(ApiResponse.build(null, message));
    }

    /** Ошибка: частный случай - 404 Not Found. */
    public static ResponseEntity<ApiResponse<Void>> notFound(String message) {
        return error(HttpStatus.NOT_FOUND, message);
    }
}
