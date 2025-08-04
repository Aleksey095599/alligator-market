package com.alligator.market.backend.instrument_catalog.currency_pair.web;

import com.alligator.market.backend.common.web.ApiResponse;
import com.alligator.market.backend.common.web.ResponseEntityFactory;
import com.alligator.market.domain.instrument.currency_pair.exception.CurrencyFromPairNotFoundException;
import com.alligator.market.domain.instrument.currency_pair.exception.DuplicatePairException;
import com.alligator.market.domain.instrument.currency_pair.exception.PairNotFoundException;
import com.alligator.market.domain.instrument.currency_pair.exception.EqualCurrenciesInPairException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Локальный обработчик исключений валютных пар.
 * Привязан к соответствующему контроллеру.
 */
@Slf4j
@RestControllerAdvice(assignableTypes = PairController.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PairExceptionHandler {

    /* Дублирование валютной пары. */
    @ExceptionHandler(DuplicatePairException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicatePair(
            DuplicatePairException ex) {

        log.warn("DuplicatePairException: {}", ex.getMessage());
        return ResponseEntityFactory.error(HttpStatus.CONFLICT, ex.getMessage());
    }

    /* Валютная пара не найдена. */
    @ExceptionHandler(PairNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handlePairNotFound(
            PairNotFoundException ex) {

        log.warn("PairNotFoundException: {}", ex.getMessage());
        return ResponseEntityFactory.notFound(ex.getMessage());
    }

    /* Одна из валют не найдена. */
    @ExceptionHandler(CurrencyFromPairNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleCurrencyNotFound(
            CurrencyFromPairNotFoundException ex) {

        log.warn("CurrencyFromPairNotFoundException: {}", ex.getMessage());
        return ResponseEntityFactory.error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /* Валютные коды в паре совпадают. */
    @ExceptionHandler(EqualCurrenciesInPairException.class)
    public ResponseEntity<ApiResponse<Void>> handleEqualCurrencies(
            EqualCurrenciesInPairException ex) {

        log.warn("EqualCurrenciesInPairException: {}", ex.getMessage());
        return ResponseEntityFactory.error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}
