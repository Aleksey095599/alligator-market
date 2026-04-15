package com.alligator.market.backend.common.web.advice;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Глобальный обработчик технических исключений REST-слоя.
 */
@Slf4j
@RestControllerAdvice
public class GlobalRestExceptionHandler {

    /* Типы ошибок в формате ProblemDetail type (RFC 7807). */
    private static final String PROBLEM_TYPE_PREFIX = "urn:alligator:problem:";
    private static final String TYPE_VALIDATION_ERROR = PROBLEM_TYPE_PREFIX + "validation-error";
    private static final String TYPE_BAD_ARGUMENT = PROBLEM_TYPE_PREFIX + "bad-argument";
    private static final String TYPE_MALFORMED_JSON = PROBLEM_TYPE_PREFIX + "malformed-json";
    private static final String TYPE_DATA_INTEGRITY_VIOLATION = PROBLEM_TYPE_PREFIX + "data-integrity-violation";
    private static final String TYPE_UNEXPECTED_ERROR = PROBLEM_TYPE_PREFIX + "unexpected-error";

    /**
     * Ошибки валидации тела запроса (@Valid) --> 422.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.warn("Validation failed: {}", ex.getMessage());

        ProblemDetail problemDetail = buildProblemDetail(
                HttpStatus.UNPROCESSABLE_CONTENT,
                "Validation failed",
                "Request validation failed",
                TYPE_VALIDATION_ERROR
        );

        /* Ошибки полей -> компактная карта field -> message. */
        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> Objects.requireNonNullElse(error.getDefaultMessage(), "Invalid value"),
                        (first, second) -> first
                ));
        problemDetail.setProperty("fieldErrors", fieldErrors);

        return problemDetail;
    }

    /**
     * Ошибки валидации параметров (@Validated) --> 400.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations()
                .stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining("; "));
        log.warn("Constraint violation: {}", message);
        return buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Bad argument",
                message,
                TYPE_BAD_ARGUMENT
        );
    }

    /**
     * Тело запроса не удалось распарсить --> 400.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.warn("Malformed JSON: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Malformed JSON",
                "Request body is malformed or unreadable",
                TYPE_MALFORMED_JSON
        );
    }

    /**
     * Нарушение целостности данных --> 409.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.warn("Data integrity violation: {}", ex.getMostSpecificCause().getMessage());
        return buildProblemDetail(
                HttpStatus.CONFLICT,
                "Data integrity violation",
                "Data integrity violation",
                TYPE_DATA_INTEGRITY_VIOLATION
        );
    }

    /**
     * Некорректно переданные аргументы --> 400.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Illegal argument: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Bad argument",
                ex.getMessage(),
                TYPE_BAD_ARGUMENT
        );
    }

    /**
     * Неподдерживаемые операции --> 500.
     */
    @ExceptionHandler({IllegalStateException.class, UnsupportedOperationException.class})
    public ProblemDetail handleIllegalState(RuntimeException ex) {
        log.error("Illegal state: {}", ex.getMessage(), ex);
        return buildProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unexpected error",
                ex.getMessage(),
                TYPE_UNEXPECTED_ERROR
        );
    }

    /**
     * Непредвиденные ошибки --> 500.
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(Exception ex) {
        log.error("Unhandled exception", ex);
        return buildProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unexpected error",
                "Unexpected server error",
                TYPE_UNEXPECTED_ERROR
        );
    }

    /* Единый builder ProblemDetail для общего контракта ошибок REST-слоя. */
    private ProblemDetail buildProblemDetail(HttpStatus status, String title, String detail, String type) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        problemDetail.setType(URI.create(type));
        return problemDetail;
    }
}
