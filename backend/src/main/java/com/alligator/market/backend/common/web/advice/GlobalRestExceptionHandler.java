package com.alligator.market.backend.common.web.advice;

import com.alligator.market.backend.common.web.dto.ApiError;
import com.alligator.market.backend.common.web.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.NoSuchElementException;

/* Глобальный обработчик исключений REST. */
@Slf4j
@RestControllerAdvice
public class GlobalRestExceptionHandler {

    /* Ошибки валидации тела запроса (@Valid). */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        List<ApiError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(ApiError::validation)
                .toList();

        log.warn("MethodArgumentNotValidException: {}", errors);

        ApiResponse<Void> body = ApiResponse.fail(
                HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
                errors,
                request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(body);
    }

    /* Ошибки валидации параметров запроса (@Validated). */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request) {

        List<ApiError> errors = ex.getConstraintViolations()
                .stream()
                .map(ApiError::validation)
                .toList();

        log.warn("ConstraintViolationException: {}", errors);

        ApiResponse<Void> body = ApiResponse.fail(
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                errors,
                request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(body);
    }

    /* Ресурс не найден. */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(
            NoSuchElementException ex,
            HttpServletRequest request) {

        log.warn("NoSuchElementException: {}", ex.getMessage());

        ApiResponse<Void> body = ApiResponse.fail(
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                List.of(ApiError.notFound(ex.getMessage())),
                request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(body);
    }

    /* Непредвиденные ошибки. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpected(
            Exception ex,
            HttpServletRequest request) {

        log.error("Unhandled exception", ex);

        ApiResponse<Void> body = ApiResponse.fail(
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                List.of(ApiError.unexpected()),
                request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body);
    }

}
