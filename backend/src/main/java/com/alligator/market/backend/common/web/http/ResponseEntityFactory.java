package com.alligator.market.backend.common.web.http;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;

/**
 * Фабрика готовых HTTP-ответов, использующих конверт {@link ApiResponse}.
 */
public final class ResponseEntityFactory {

    private ResponseEntityFactory() {
    }

    /** Успех: 200 OK. */
    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    /** Успех: 201 Created. */
    public static <T> ResponseEntity<ApiResponse<T>> created(URI location, T data) {
        return ResponseEntity.created(location).body(ApiResponse.ok(data));
    }

    /**
     * Успех: 204 No Content без тела.
     */
    public static ResponseEntity<Void> noContent() {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /** Ошибка: общий конструктор. */
    public static ResponseEntity<ApiResponse<Void>> error(HttpStatus status, String code, String message) {
        return ResponseEntity.status(status).body(ApiResponse.error(code, message));
    }

    /** Ошибка: 400 Bad Request. */
    public static ResponseEntity<ApiResponse<Void>> badRequest(String code, String message) {
        return error(HttpStatus.BAD_REQUEST, code, message);
    }

    /** Ошибка: 404 Not Found. */
    public static ResponseEntity<ApiResponse<Void>> notFound(String code, String message) {
        return error(HttpStatus.NOT_FOUND, code, message);
    }

    /** Ошибка: 409 Conflict. */
    public static ResponseEntity<ApiResponse<Void>> conflict(String code, String message) {
        return error(HttpStatus.CONFLICT, code, message);
    }

    /** Ошибка: 422 Unprocessable Entity. */
    public static ResponseEntity<ApiResponse<Void>> unprocessableEntity(String code, String message) {
        return error(HttpStatus.UNPROCESSABLE_ENTITY, code, message);
    }
}
