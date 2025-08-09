package com.alligator.market.backend.common.web;

import com.alligator.market.domain.instrument.type.fx.reference.currency.catalog.exeption.CurrencyUsedInPairsException;
import com.alligator.market.domain.instrument.type.fx.reference.currency.catalog.exeption.DuplicateCurrencyException;
import com.alligator.market.domain.instrument.type.fx.reference.currency_pair.catalog.exeption.CurrencyFromPairNotFoundException;
import com.alligator.market.domain.instrument.type.fx.reference.currency_pair.catalog.exeption.DuplicatePairException;
import com.alligator.market.domain.instrument.type.fx.reference.currency_pair.catalog.exeption.EqualCurrenciesInPairException;
import com.alligator.market.domain.provider.model.InstrumentNotSupportedException;
import com.alligator.market.domain.provider.context_sync.DuplicateProviderProfileInContextException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Централизованный обработчик доменных исключений.
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ApplicationExceptionHandler {

    /** Дублирование валюты. */
    @ExceptionHandler(DuplicateCurrencyException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateCurrency(DuplicateCurrencyException ex) {
        // Отдаем 409, так как запись уже существует
        log.warn("DuplicateCurrencyException: {}", ex.getMessage());
        return ResponseEntityFactory.error(HttpStatus.CONFLICT, ex.getMessage());
    }

    /** Валюта используется в валютных парах. */
    @ExceptionHandler(CurrencyUsedInPairsException.class)
    public ResponseEntity<ApiResponse<Void>> handleCurrencyUsed(CurrencyUsedInPairsException ex) {
        // 409, ресурс занят
        log.warn("CurrencyUsedInPairsException: {}", ex.getMessage());
        return ResponseEntityFactory.error(HttpStatus.CONFLICT, ex.getMessage());
    }

    /** Дублирование валютной пары. */
    @ExceptionHandler(DuplicatePairException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicatePair(DuplicatePairException ex) {
        // 409 при попытке создать существующую пару
        log.warn("DuplicatePairException: {}", ex.getMessage());
        return ResponseEntityFactory.error(HttpStatus.CONFLICT, ex.getMessage());
    }

    /** Одна из валют компонента пары не найдена. */
    @ExceptionHandler(CurrencyFromPairNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleCurrencyFromPairNotFound(CurrencyFromPairNotFoundException ex) {
        // 400, неверные данные пары
        log.warn("CurrencyFromPairNotFoundException: {}", ex.getMessage());
        return ResponseEntityFactory.error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /** Валютные коды в паре совпадают. */
    @ExceptionHandler(EqualCurrenciesInPairException.class)
    public ResponseEntity<ApiResponse<Void>> handleEqualCurrencies(EqualCurrenciesInPairException ex) {
        // 400, клиентская ошибка
        log.warn("EqualCurrenciesInPairException: {}", ex.getMessage());
        return ResponseEntityFactory.error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /** Профиль провайдера уже присутствует в контексте. */
    @ExceptionHandler(DuplicateProviderProfileInContextException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateProviderProfile(DuplicateProviderProfileInContextException ex) {
        // 409, дубликат профиля
        log.warn("DuplicateProviderProfileInContextException: {}", ex.getMessage());
        return ResponseEntityFactory.error(HttpStatus.CONFLICT, ex.getMessage());
    }

    /** Тип инструмента не поддерживается провайдером. */
    @ExceptionHandler(InstrumentNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleInstrumentNotSupported(InstrumentNotSupportedException ex) {
        // 400, неподдерживаемый тип инструмента
        log.warn("InstrumentNotSupportedException: {}", ex.getMessage());
        return ResponseEntityFactory.error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}
