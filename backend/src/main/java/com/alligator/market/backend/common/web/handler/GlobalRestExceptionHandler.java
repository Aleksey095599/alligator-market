package com.alligator.market.backend.common.web.handler;

import com.alligator.market.backend.common.web.ApiResponse;
import com.alligator.market.backend.common.web.ResponseEntityFactory;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Глобальный обработчик исключений REST-слоя.
 */
@Slf4j
@RestControllerAdvice
public class GlobalRestExceptionHandler {

    /** Ошибки валидации тела запроса (@Valid) 422. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        // Собираем все ошибки валидации в одно сообщение
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        log.warn("MethodArgumentNotValidException: {}", message);
        return ResponseEntityFactory.error(
                HttpStatus.UNPROCESSABLE_ENTITY,
                message
        );
    }

    /** Ошибки валидации параметров (@Validated) 400. */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException ex) {
        // Собираем все ошибки валидации в одно сообщение
        String message = ex.getConstraintViolations()
                .stream()
                .map(v -> v.getPropertyPath() + " " + v.getMessage())
                .collect(Collectors.joining("; "));

        log.warn("ConstraintViolationException: {}", message);
        return ResponseEntityFactory.error(
                HttpStatus.BAD_REQUEST,
                message
        );
    }

    /** Тело запроса не удалось распарсить 400. */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getMostSpecificCause();
        String message = cause.getMessage();
        log.warn("HttpMessageNotReadableException: {}", message);
        return ResponseEntityFactory.error(
                HttpStatus.BAD_REQUEST,
                "Request body is not readable"
        );
    }

    /** Ошибки клиента при передаче аргументов 400. */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("IllegalArgumentException: {}", ex.getMessage());
        return ResponseEntityFactory.error(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
    }

    /** Конфликт целостности данных 409. */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.warn("DataIntegrityViolationException: {}", ex.getMessage());
        return ResponseEntityFactory.error(
                HttpStatus.CONFLICT,
                "Resource already exists or violates integrity constraints"
        );
    }

    /** Некорректное состояние сервера 500. */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalState(IllegalStateException ex) {
        log.error("IllegalStateException: {}", ex.getMessage());
        return ResponseEntityFactory.error(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage()
        );
    }

    /** Непредвиденные ошибки 500. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpected(Exception ex) {
        log.error("Unhandled exception", ex);
        return ResponseEntityFactory.error(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unexpected server error"
        );
    }
}
