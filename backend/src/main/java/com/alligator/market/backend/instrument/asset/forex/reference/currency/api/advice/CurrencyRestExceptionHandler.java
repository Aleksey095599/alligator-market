package com.alligator.market.backend.instrument.asset.forex.reference.currency.api.advice;

import com.alligator.market.backend.instrument.asset.forex.reference.currency.api.command.create.controller.CreateCurrencyController;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.api.command.delete.controller.DeleteCurrencyController;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.api.command.update.controller.UpdateCurrencyController;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.api.query.list.controller.ListCurrenciesController;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.exception.CurrencyAlreadyExistsException;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.exception.CurrencyInUseException;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.exception.CurrencyNameDuplicateException;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.exception.CurrencyNotFoundException;
import com.alligator.market.domain.common.exception.DomainErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Локальный обработчик ошибок currency feature для REST-контроллеров.
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(assignableTypes = {
        CreateCurrencyController.class,
        UpdateCurrencyController.class,
        DeleteCurrencyController.class,
        ListCurrenciesController.class
})
public class CurrencyRestExceptionHandler {

    /* Локальные коды ошибок HTTP/DTO для currency feature. */
    private static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    private static final String MALFORMED_REQUEST = "MALFORMED_REQUEST";

    /**
     * Валюта с таким кодом уже существует --> 409.
     */
    @ExceptionHandler(CurrencyAlreadyExistsException.class)
    public ProblemDetail currencyAlreadyExists(CurrencyAlreadyExistsException ex) {
        log.warn("Currency already exists: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.CONFLICT,
                "Currency already exists",
                ex.getMessage(),
                DomainErrorCode.CURRENCY_ALREADY_EXISTS.name()
        );
    }

    /**
     * Валюта с таким именем уже существует --> 409.
     */
    @ExceptionHandler(CurrencyNameDuplicateException.class)
    public ProblemDetail currencyNameDuplicate(CurrencyNameDuplicateException ex) {
        log.warn("Currency name duplicate: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.CONFLICT,
                "Currency name duplicate",
                ex.getMessage(),
                DomainErrorCode.CURRENCY_NAME_DUPLICATE.name()
        );
    }

    /**
     * Валюта используется внешними фичами/агрегатами --> 409.
     */
    @ExceptionHandler(CurrencyInUseException.class)
    public ProblemDetail currencyInUse(CurrencyInUseException ex) {
        log.warn("Currency is in use: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.CONFLICT,
                "Currency is in use",
                ex.getMessage(),
                DomainErrorCode.CURRENCY_IN_USE.name()
        );
    }

    /**
     * Валюта не найдена --> 404.
     */
    @ExceptionHandler(CurrencyNotFoundException.class)
    public ProblemDetail currencyNotFound(CurrencyNotFoundException ex) {
        log.warn("Currency not found: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.NOT_FOUND,
                "Currency not found",
                ex.getMessage(),
                DomainErrorCode.CURRENCY_NOT_FOUND.name()
        );
    }

    /**
     * Ошибки валидации входного DTO (@Valid) --> 400.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.warn("Request validation failed: {}", ex.getMessage());

        ProblemDetail problemDetail = buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                "Request validation failed",
                VALIDATION_ERROR
        );

        /* Ошибки полей -> компактная карта field -> message. */
        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> Objects.requireNonNullElse(fieldError.getDefaultMessage(), "Invalid value"),
                        (first, second) -> first
                ));
        problemDetail.setProperty("fieldErrors", fieldErrors);

        return problemDetail;
    }

    /**
     * Некорректное тело запроса (JSON parse / type mismatch) --> 400.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.warn("Malformed request body: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Malformed request",
                "Request body is malformed or unreadable",
                MALFORMED_REQUEST
        );
    }

    /* Общий builder ProblemDetail для единообразного контракта ошибок currency feature. */
    private ProblemDetail buildProblemDetail(HttpStatus status, String title, String detail, String errorCode) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        problemDetail.setProperty("errorCode", errorCode);
        return problemDetail;
    }
}
