package com.alligator.market.backend.common.web.handler;

import com.alligator.market.backend.common.web.ApiResponse;
import com.alligator.market.backend.common.web.ResponseEntityFactory;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxSpotCurrencyNotFoundException;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxSpotDuplicateException;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxSpotSameCurrenciesException;
import com.alligator.market.domain.instrument.type.forex.spot.reference.currency.exception.CurrencyUsedInFxSpotException;
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
    @ExceptionHandler(CurrencyUsedInFxSpotException.class)
    public ResponseEntity<ApiResponse<Void>> handleCurrencyUsed(CurrencyUsedInFxSpotException ex) {
        // 409, ресурс занят
        log.warn("CurrencyUsedInFxSpotException: {}", ex.getMessage());
        return ResponseEntityFactory.error(HttpStatus.CONFLICT, ex.getMessage());
    }

    /** Дублирование инструмента FX_OUTRIGHT. */
    @ExceptionHandler(FxSpotDuplicateException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateFxOutright(FxSpotDuplicateException ex) {
        // 409 при попытке создать существующий инструмент
        log.warn("FxSpotDuplicateException: {}", ex.getMessage());
        return ResponseEntityFactory.error(HttpStatus.CONFLICT, ex.getMessage());
    }

    /** Одна из валют компонента FX_OUTRIGHT не найдена. */
    @ExceptionHandler(FxSpotCurrencyNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleCurrencyFromFxOutrightNotFound(FxSpotCurrencyNotFoundException ex) {
        // 400, неверные данные инструмента
        log.warn("FxSpotCurrencyNotFoundException: {}", ex.getMessage());
        return ResponseEntityFactory.error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /** Базовая и котируемая валюты совпадают. */
    @ExceptionHandler(FxSpotSameCurrenciesException.class)
    public ResponseEntity<ApiResponse<Void>> handleSameCurrencies(FxSpotSameCurrenciesException ex) {
        // 400, клиентская ошибка
        log.warn("FxSpotSameCurrenciesException: {}", ex.getMessage());
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
