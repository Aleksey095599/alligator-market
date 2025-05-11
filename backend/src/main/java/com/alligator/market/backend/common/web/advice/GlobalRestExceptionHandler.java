package com.alligator.market.backend.common.web.advice;

import com.alligator.market.backend.common.web.dto.ApiResponse;
import com.alligator.market.backend.common.web.util.ResponseEntityFactory;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/* Глобальный обработчик исключений REST-слоя. */
@Slf4j
@RestControllerAdvice
public class GlobalRestExceptionHandler {

    /* Ошибки валидации тела запроса (@Valid) */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        // Собираем все ошибки валидации в одно сообщение
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        log.warn("MethodArgumentNotValidException: {}", message);
        return ResponseEntityFactory.error(HttpStatus.UNPROCESSABLE_ENTITY, message);
    }

    /* Ошибки валидации параметров (@Validated) */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException ex) {
        // Собираем все ошибки валидации в одно сообщение
        String message = ex.getConstraintViolations()
                .stream()
                .map(v -> v.getPropertyPath() + " " + v.getMessage())
                .collect(Collectors.joining("; "));

        log.warn("ConstraintViolationException: {}", message);
        return ResponseEntityFactory.error(HttpStatus.BAD_REQUEST, message);
    }

    /* Конфликт целостности данных (например, нарушение уникального индекса) */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.warn("DataIntegrityViolationException: {}", ex.getMessage());
        return ResponseEntityFactory.error(
                HttpStatus.CONFLICT,
                "Resource already exists or violates integrity constraints"
        );
    }

    /* Ресурс не найден */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(NoSuchElementException ex) {
        log.warn("NoSuchElementException: {}", ex.getMessage());
        return ResponseEntityFactory.notFound(ex.getMessage());
    }

    /* Непредвиденные ошибки */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpected(Exception ex) {
        log.error("Unhandled exception", ex);
        return ResponseEntityFactory.error(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error");
    }

}
