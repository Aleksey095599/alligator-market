package com.alligator.market.backend.common.web.advice;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Глобальный обработчик технических исключений REST-слоя.
 */
@Slf4j
@RestControllerAdvice
public class GlobalRestExceptionHandler {

    /**
     * Ошибки валидации тела запроса (@Valid) --> 422.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.warn("Validation failed: {}", ex.getMessage());

        ProblemDetail problemDetail = buildProblemDetail(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Validation failed",
                "Request validation failed",
                GlobalErrorCodes.VALIDATION_ERROR.name()
        );

        /* Ошибки полей -> компактная карта field -> message. */
        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        error -> error.getField(),
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
                GlobalErrorCodes.BAD_ARGUMENT.name()
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
                GlobalErrorCodes.MALFORMED_JSON.name()
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
                GlobalErrorCodes.DATA_INTEGRITY_VIOLATION.name()
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
                GlobalErrorCodes.BAD_ARGUMENT.name()
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
                GlobalErrorCodes.UNEXPECTED_ERROR.name()
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
                GlobalErrorCodes.UNEXPECTED_ERROR.name()
        );
    }

    /* Единый builder ProblemDetail для общего контракта ошибок REST-слоя. */
    private ProblemDetail buildProblemDetail(HttpStatus status, String title, String detail, String errorCode) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        problemDetail.setProperty("errorCode", errorCode);
        return problemDetail;
    }
}
