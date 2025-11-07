package com.alligator.market.backend.common.web.handler;

import com.alligator.market.backend.common.web.http.ApiResponse;
import com.alligator.market.backend.common.web.http.ErrorCode;
import com.alligator.market.backend.common.web.http.ResponseEntityFactory;
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
 * Глобальный обработчик технических исключений REST-слоя.
 */
@Slf4j
@RestControllerAdvice
public class GlobalRestExceptionHandler {

    /** Ошибки валидации тела запроса (@Valid) → 422. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.warn("Validation failed: {}", message);
        return ResponseEntityFactory.unprocessableEntity(ErrorCode.VALIDATION_ERROR.name(), message);
    }

    /** Ошибки валидации параметров (@Validated) → 400. */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations()
                .stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining("; "));
        log.warn("Constraint violation: {}", message);
        return ResponseEntityFactory.badRequest(ErrorCode.BAD_ARGUMENT.name(), message);
    }

    /** Тело запроса не удалось распарсить → 400. */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.warn("Malformed JSON: {}", ex.getMessage());
        return ResponseEntityFactory.badRequest(ErrorCode.MALFORMED_JSON.name(), "Malformed JSON");
    }

    /** Нарушение целостности данных → 409. */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.warn("Data integrity violation: {}", ex.getMostSpecificCause().getMessage());
        return ResponseEntityFactory.conflict(ErrorCode.DATA_INTEGRITY_VIOLATION.name(), "Data integrity violation");
    }

    /** Некорректно переданные аргументы → 400. */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Illegal argument: {}", ex.getMessage());
        return ResponseEntityFactory.badRequest(ErrorCode.BAD_ARGUMENT.name(), ex.getMessage());
    }

    /** Неподдерживаемые операции → 500. */
    @ExceptionHandler({IllegalStateException.class, UnsupportedOperationException.class})
    public ResponseEntity<ApiResponse<Void>> handleIllegalState(RuntimeException ex) {
        log.error("Illegal state: {}", ex.getMessage(), ex);
        return ResponseEntityFactory.error(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.UNEXPECTED_ERROR.name(), ex.getMessage());
    }

    /** Непредвиденные ошибки → 500. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpected(Exception ex) {
        log.error("Unhandled exception", ex);
        return ResponseEntityFactory.error(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.UNEXPECTED_ERROR.name(), "Unexpected server error");
    }
}
