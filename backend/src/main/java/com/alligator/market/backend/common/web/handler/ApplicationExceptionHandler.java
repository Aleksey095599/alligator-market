package com.alligator.market.backend.common.web.handler;

import com.alligator.market.backend.common.web.ApiResponse;
import com.alligator.market.backend.common.web.ResponseEntityFactory;
import com.alligator.market.domain.instrument.type.forex.outright.catalog.exception.CurrencyFromFxOutrightNotFoundException;
import com.alligator.market.domain.instrument.type.forex.outright.catalog.exception.DuplicateFxOutrightException;
import com.alligator.market.domain.instrument.type.forex.outright.catalog.exception.SameCurrenciesException;
import com.alligator.market.domain.instrument.type.forex.outright.reference.currency.catalog.exception.CurrencyUsedInFxOutrightException;
import com.alligator.market.domain.instrument.type.forex.outright.reference.currency.catalog.exception.DuplicateCurrencyException;
import com.alligator.market.domain.provider.model.InstrumentNotSupportedException;
import com.alligator.market.domain.provider.profile.context.DuplicateProfileInContextException;
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

    /** Валюта используется в инструментах FX_OUTRIGHT. */
    @ExceptionHandler(CurrencyUsedInFxOutrightException.class)
    public ResponseEntity<ApiResponse<Void>> handleCurrencyUsed(CurrencyUsedInFxOutrightException ex) {
        // 409, ресурс занят
        log.warn("CurrencyUsedInFxOutrightException: {}", ex.getMessage());
        return ResponseEntityFactory.error(HttpStatus.CONFLICT, ex.getMessage());
    }

    /** Дублирование инструмента FX_OUTRIGHT. */
    @ExceptionHandler(DuplicateFxOutrightException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateFxOutright(DuplicateFxOutrightException ex) {
        // 409 при попытке создать существующий инструмент
        log.warn("DuplicateFxOutrightException: {}", ex.getMessage());
        return ResponseEntityFactory.error(HttpStatus.CONFLICT, ex.getMessage());
    }

    /** Одна из валют компонента FX_OUTRIGHT не найдена. */
    @ExceptionHandler(CurrencyFromFxOutrightNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleCurrencyFromFxOutrightNotFound(CurrencyFromFxOutrightNotFoundException ex) {
        // 400, неверные данные инструмента
        log.warn("CurrencyFromFxOutrightNotFoundException: {}", ex.getMessage());
        return ResponseEntityFactory.error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /** Базовая и котируемая валюты совпадают. */
    @ExceptionHandler(SameCurrenciesException.class)
    public ResponseEntity<ApiResponse<Void>> handleSameCurrencies(SameCurrenciesException ex) {
        // 400, клиентская ошибка
        log.warn("SameCurrenciesException: {}", ex.getMessage());
        return ResponseEntityFactory.error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /** Профиль провайдера уже присутствует в контексте. */
    @ExceptionHandler(DuplicateProfileInContextException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateProviderProfile(DuplicateProfileInContextException ex) {
        // 409, дубликат профиля
        log.warn("DuplicateProfileInContextException: {}", ex.getMessage());
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
