package com.alligator.market.backend.common.web.handler;

import com.alligator.market.backend.common.web.ApiResponse;
import com.alligator.market.backend.common.web.ResponseEntityFactory;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxOutrightCurrencyNotFoundException;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxOutrightDuplicateException;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxOutrightSameCurrenciesException;
import com.alligator.market.domain.instrument.type.forex.spot.reference.currency.exception.CurrencyUsedInFxOutrightException;
import com.alligator.market.domain.instrument.type.forex.spot.reference.currency.exception.CurrencyDuplicateException;
import com.alligator.market.domain.provider.exception.InstrumentNotSupportedException;
import com.alligator.market.domain.provider.exception.ProviderHandlersInvalidException;
import com.alligator.market.domain.provider.exception.ProviderHandlerMismatchException;
import com.alligator.market.domain.provider.exception.ProviderInstrumentHandlerDuplicateException;
import com.alligator.market.domain.provider.sync.exeption.ContextProfileDuplicateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Централизованный обработчик доменных исключений, относящихся к бизнес-логике приложения.
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ApplicationExceptionHandler {

    /** Дублирование валюты. */
    @ExceptionHandler(CurrencyDuplicateException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateCurrency(CurrencyDuplicateException ex) {
        // Отдаем 409, так как запись уже существует
        log.warn("CurrencyDuplicateException: {}", ex.getMessage());
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
    @ExceptionHandler(FxOutrightDuplicateException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateFxOutright(FxOutrightDuplicateException ex) {
        // 409 при попытке создать существующий инструмент
        log.warn("FxOutrightDuplicateException: {}", ex.getMessage());
        return ResponseEntityFactory.error(HttpStatus.CONFLICT, ex.getMessage());
    }

    /** Одна из валют компонента FX_OUTRIGHT не найдена. */
    @ExceptionHandler(FxOutrightCurrencyNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleCurrencyFromFxOutrightNotFound(FxOutrightCurrencyNotFoundException ex) {
        // 400, неверные данные инструмента
        log.warn("FxOutrightCurrencyNotFoundException: {}", ex.getMessage());
        return ResponseEntityFactory.error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /** Базовая и котируемая валюты совпадают. */
    @ExceptionHandler(FxOutrightSameCurrenciesException.class)
    public ResponseEntity<ApiResponse<Void>> handleSameCurrencies(FxOutrightSameCurrenciesException ex) {
        // 400, клиентская ошибка
        log.warn("FxOutrightSameCurrenciesException: {}", ex.getMessage());
        return ResponseEntityFactory.error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /** Профиль провайдера уже присутствует в контексте. */
    @ExceptionHandler(ContextProfileDuplicateException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateProviderProfile(ContextProfileDuplicateException ex) {
        // 409, дубликат профиля
        log.warn("ContextProfileDuplicateException: {}", ex.getMessage());
        return ResponseEntityFactory.error(HttpStatus.CONFLICT, ex.getMessage());
    }

    /** Тип инструмента не поддерживается провайдером. */
    @ExceptionHandler(InstrumentNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleInstrumentNotSupported(InstrumentNotSupportedException ex) {
        // 400, неподдерживаемый тип инструмента
        log.warn("InstrumentNotSupportedException: {}", ex.getMessage());
        return ResponseEntityFactory.error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /** Некорректный набор обработчиков провайдера. */
    @ExceptionHandler(ProviderHandlersInvalidException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidHandlers(ProviderHandlersInvalidException ex) {
        // 500, ошибка конфигурации сервера
        log.error("ProviderHandlersInvalidException: {}", ex.getMessage());
        return ResponseEntityFactory.error(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    /** Обработчик относится к другому провайдеру. */
    @ExceptionHandler(ProviderHandlerMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleHandlerMismatch(ProviderHandlerMismatchException ex) {
        // 500, ошибка конфигурации сервера
        log.error("ProviderHandlerMismatchException: {}", ex.getMessage());
        return ResponseEntityFactory.error(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    /** Дублирование обработчика по типу инструмента. */
    @ExceptionHandler(ProviderInstrumentHandlerDuplicateException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateHandler(ProviderInstrumentHandlerDuplicateException ex) {
        // 500, ошибка конфигурации сервера
        log.error("ProviderInstrumentHandlerDuplicateException: {}", ex.getMessage());
        return ResponseEntityFactory.error(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
}
