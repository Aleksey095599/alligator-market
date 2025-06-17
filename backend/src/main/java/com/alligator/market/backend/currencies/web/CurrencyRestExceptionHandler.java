package com.alligator.market.backend.currencies.web;

import com.alligator.market.backend.common.web.dto.ApiResponse;
import com.alligator.market.backend.common.web.util.ResponseEntityFactory;
import com.alligator.market.backend.currencies.exceptions.CurrencyNotFoundException;
import com.alligator.market.backend.currencies.exceptions.CurrencyUsedInPairsException;
import com.alligator.market.backend.currencies.exceptions.DuplicateCurrencyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/* Локальный обработчик исключений пакета «Currency».
   → Привязан к контроллеру для валют. */
@Slf4j
@RestControllerAdvice(assignableTypes = CurrencyController.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CurrencyRestExceptionHandler {

    /* Дублирование по одному из уникальных параметров валюты. */
    @ExceptionHandler(DuplicateCurrencyException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateCurrency(
            DuplicateCurrencyException ex) {

        log.warn("DuplicateCurrencyException: {}", ex.getMessage());
        return ResponseEntityFactory.error(HttpStatus.CONFLICT, ex.getMessage());
    }

    /* Валюта с указанным кодом не найдена. */
    @ExceptionHandler(CurrencyNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleCurrencyNotFound(
            CurrencyNotFoundException ex) {

        log.warn("CurrencyNotFoundException: {}", ex.getMessage());
        return ResponseEntityFactory.notFound(ex.getMessage());
    }

    /* Валюта используется в валютных парах. */
    @ExceptionHandler(CurrencyUsedInPairsException.class)
    public ResponseEntity<ApiResponse<Void>> handleCurrencyUsed(
            CurrencyUsedInPairsException ex) {

        log.warn("CurrencyUsedInPairsException: {}", ex.getMessage());
        return ResponseEntityFactory.error(HttpStatus.CONFLICT, ex.getMessage());
    }

}
