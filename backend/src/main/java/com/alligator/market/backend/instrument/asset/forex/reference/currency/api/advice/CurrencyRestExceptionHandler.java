package com.alligator.market.backend.instrument.asset.forex.reference.currency.api.advice;

import com.alligator.market.backend.instrument.asset.forex.reference.currency.api.command.create.controller.CreateCurrencyController;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.api.command.delete.controller.DeleteCurrencyController;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.api.command.update.controller.UpdateCurrencyController;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.api.query.list.controller.ListCurrenciesController;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.exception.CurrencyAlreadyExistsException;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.exception.CurrencyInUseException;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.exception.CurrencyNameDuplicateException;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.exception.CurrencyNotFoundException;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.api.error.CurrencyApiErrorCodes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Feature-specific обработчик ошибок API.
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
                CurrencyApiErrorCodes.CURRENCY_ALREADY_EXISTS
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
                CurrencyApiErrorCodes.CURRENCY_NAME_DUPLICATE
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
                CurrencyApiErrorCodes.CURRENCY_IN_USE
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
                CurrencyApiErrorCodes.CURRENCY_NOT_FOUND
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
