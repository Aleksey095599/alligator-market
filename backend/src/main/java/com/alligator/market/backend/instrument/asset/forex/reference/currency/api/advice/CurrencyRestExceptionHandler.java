package com.alligator.market.backend.instrument.asset.forex.reference.currency.api.advice;

import com.alligator.market.backend.common.web.response.ApiResponse;
import com.alligator.market.backend.common.web.response.ResponseEntityFactory;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Локальный обработчик ошибок currency-feature для REST-контроллеров.
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
    public ResponseEntity<ApiResponse<Void>> currencyAlreadyExists(CurrencyAlreadyExistsException ex) {
        log.warn("Currency already exists: {}", ex.getMessage());
        return ResponseEntityFactory.conflict(DomainErrorCode.CURRENCY_ALREADY_EXISTS.name(), ex.getMessage());
    }

    /**
     * Валюта с таким именем уже существует --> 409.
     */
    @ExceptionHandler(CurrencyNameDuplicateException.class)
    public ResponseEntity<ApiResponse<Void>> currencyNameDuplicate(CurrencyNameDuplicateException ex) {
        log.warn("Currency name duplicate: {}", ex.getMessage());
        return ResponseEntityFactory.conflict(DomainErrorCode.CURRENCY_NAME_DUPLICATE.name(), ex.getMessage());
    }

    /**
     * Валюта используется внешними фичами/агрегатами --> 409.
     */
    @ExceptionHandler(CurrencyInUseException.class)
    public ResponseEntity<ApiResponse<Void>> currencyInUse(CurrencyInUseException ex) {
        log.warn("Currency is in use: {}", ex.getMessage());
        return ResponseEntityFactory.conflict(DomainErrorCode.CURRENCY_IN_USE.name(), ex.getMessage());
    }

    /**
     * Валюта не найдена --> 404.
     */
    @ExceptionHandler(CurrencyNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> currencyNotFound(CurrencyNotFoundException ex) {
        log.warn("Currency not found: {}", ex.getMessage());
        return ResponseEntityFactory.notFound(DomainErrorCode.CURRENCY_NOT_FOUND.name(), ex.getMessage());
    }
}
