package com.alligator.market.backend.currency.web;

import com.alligator.market.backend.common.web.dto.ApiError;
import com.alligator.market.backend.common.web.dto.ApiResponse;
import com.alligator.market.backend.currency.service.exceptions.DuplicateCurrencyException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/* Локальный обработчик исключений для функций «Currency».
   → Ограничиваем область действия пакетом currency,
   чтобы не перехватывать ошибки других доменов. */
@Slf4j
@RestControllerAdvice(basePackages = "com.alligator.market.backend.currency")
public class CurrencyRestExceptionHandler {

    /* Дублирующаяся валюта (код/название уже существует). */
    @ExceptionHandler(DuplicateCurrencyException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateCurrency(
            DuplicateCurrencyException ex,
            HttpServletRequest request) {

        log.warn("DuplicateCurrencyException: {}", ex.getMessage());

        ApiResponse<Void> body = ApiResponse.fail(
                HttpStatus.CONFLICT.getReasonPhrase(),
                // Одна запись оборачивается в List<> для соответствия контракту ApiResponse
                List.of(new ApiError("DUPLICATE_CURRENCY", ex.getMessage(), null)),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409 – стандарт для конфликтов/дубликатов
                .body(body);
    }

}
