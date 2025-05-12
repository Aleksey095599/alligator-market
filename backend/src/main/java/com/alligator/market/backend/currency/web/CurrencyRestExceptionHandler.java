package com.alligator.market.backend.currency.web;

import com.alligator.market.backend.common.web.dto.ApiResponse;
import com.alligator.market.backend.common.web.util.ResponseEntityFactory;
import com.alligator.market.backend.currency.service.exceptions.DuplicateCurrencyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/* Локальный обработчик исключений пакета «Currency».
   → Привязан к контроллеру для валют. */
@Slf4j
@RestControllerAdvice(assignableTypes = CurrencyController.class)
public class CurrencyRestExceptionHandler {

    /* Дублирование по одному из уникальных параметров валюты. */
    @ExceptionHandler(DuplicateCurrencyException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateCurrency(
            DuplicateCurrencyException ex) {

        log.warn("DuplicateCurrencyException: {}", ex.getMessage());
        return ResponseEntityFactory.error(HttpStatus.CONFLICT, ex.getMessage());
    }

}
