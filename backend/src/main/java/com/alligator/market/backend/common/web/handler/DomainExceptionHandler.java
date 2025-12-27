package com.alligator.market.backend.common.web.handler;

import com.alligator.market.backend.common.web.http.ApiResponse;
import com.alligator.market.backend.common.web.http.ErrorCode;
import com.alligator.market.backend.common.web.http.ResponseEntityFactory;
import com.alligator.market.domain.instrument.type.forex.currency.exception.*;
import com.alligator.market.domain.instrument.type.forex.spot.exception.*;
import com.alligator.market.domain.provider.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Централизованный обработчик доменных исключений приложения.
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class DomainExceptionHandler {

    /**
     * Валюта с таким кодом уже существует --> 409.
     */
    @ExceptionHandler(CurrencyAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> currencyAlreadyExists(CurrencyAlreadyExistsException ex) {
        log.warn("Currency already exists: {}", ex.getMessage());
        return ResponseEntityFactory.conflict(ErrorCode.CURRENCY_ALREADY_EXISTS.name(), ex.getMessage());
    }

    /**
     * Валюта с таким именем уже существует --> 409.
     */
    @ExceptionHandler(CurrencyNameDuplicateException.class)
    public ResponseEntity<ApiResponse<Void>> currencyNameDuplicate(CurrencyNameDuplicateException ex) {
        log.warn("Currency name duplicate: {}", ex.getMessage());
        return ResponseEntityFactory.conflict(ErrorCode.CURRENCY_NAME_DUPLICATE.name(), ex.getMessage());
    }

    /**
     * Валюта используется в инструментах FX_SPOT --> 409.
     */
    @ExceptionHandler(CurrencyUsedInFxSpotException.class)
    public ResponseEntity<ApiResponse<Void>> currencyUsedInFxSpot(CurrencyUsedInFxSpotException ex) {
        log.warn("Currency used in FX Spot: {}", ex.getMessage());
        return ResponseEntityFactory.conflict(ErrorCode.CURRENCY_USED_IN_FX_SPOT.name(), ex.getMessage());
    }

    /**
     * Валюта не найдена --> 404.
     */
    @ExceptionHandler(CurrencyNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> currencyNotFound(CurrencyNotFoundException ex) {
        log.warn("Currency not found: {}", ex.getMessage());
        return ResponseEntityFactory.notFound(ErrorCode.CURRENCY_NOT_FOUND.name(), ex.getMessage());
    }

    /**
     * Валюта уже используется при создании FX_SPOT --> 409.
     */
    @ExceptionHandler(FxSpotAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> fxSpotAlreadyExists(FxSpotAlreadyExistsException ex) {
        log.warn("FX Spot already exists: {}", ex.getMessage());
        return ResponseEntityFactory.conflict(ErrorCode.FX_SPOT_ALREADY_EXISTS.name(), ex.getMessage());
    }

    /**
     * Инструмент FX_SPOT не найден --> 404.
     */
    @ExceptionHandler(FxSpotNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> fxSpotNotFound(FxSpotNotFoundException ex) {
        log.warn("FX Spot not found: {}", ex.getMessage());
        return ResponseEntityFactory.notFound(ErrorCode.FX_SPOT_NOT_FOUND.name(), ex.getMessage());
    }

    /**
     * Указаны одинаковые валюты для FX_SPOT --> 400.
     */
    @ExceptionHandler(FxSpotSameCurrenciesException.class)
    public ResponseEntity<ApiResponse<Void>> fxSpotSameCurrencies(FxSpotSameCurrenciesException ex) {
        log.warn("FX Spot same currencies: {}", ex.getMessage());
        return ResponseEntityFactory.badRequest(ErrorCode.FX_SPOT_SAME_CURRENCIES.name(), ex.getMessage());
    }

    /**
     * Инструмент не поддерживается провайдером --> 400.
     */
    @ExceptionHandler(InstrumentNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> instrumentNotSupported(InstrumentNotSupportedException ex) {
        log.warn("Instrument not supported: {}", ex.getMessage());
        return ResponseEntityFactory.badRequest(ErrorCode.INSTRUMENT_NOT_SUPPORTED.name(), ex.getMessage());
    }

    /**
     * Обработчик инструмента не найден --> 404.
     */
    @ExceptionHandler(HandlerNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handlerNotFound(HandlerNotFoundException ex) {
        log.warn("Handler not found: {}", ex.getMessage());
        return ResponseEntityFactory.notFound(ErrorCode.HANDLER_NOT_FOUND.name(), ex.getMessage());
    }

    /**
     * Некорректный класс или тип инструмента --> 400.
     */
    @ExceptionHandler({InstrumentWrongClassException.class, InstrumentWrongTypeException.class})
    public ResponseEntity<ApiResponse<Void>> instrumentWrong(RuntimeException ex) {
        log.warn("Instrument mismatch: {}", ex.getMessage());
        String code = ex instanceof InstrumentWrongClassException
                ? ErrorCode.INSTRUMENT_WRONG_CLASS.name()
                : ErrorCode.INSTRUMENT_WRONG_TYPE.name();
        return ResponseEntityFactory.badRequest(code, ex.getMessage());
    }

    /**
     * Дублирование кода провайдера --> 409.
     */
    @ExceptionHandler(ProviderCodeDuplicateException.class)
    public ResponseEntity<ApiResponse<Void>> providerCodeDuplicate(ProviderCodeDuplicateException ex) {
        log.warn("Provider code duplicate: {}", ex.getMessage());
        return ResponseEntityFactory.conflict(ErrorCode.PROVIDER_CODE_DUPLICATE.name(), ex.getMessage());
    }

    /**
     * Дублирование отображаемого имени провайдера --> 409.
     */
    @ExceptionHandler(ProviderDisplayNameDuplicateException.class)
    public ResponseEntity<ApiResponse<Void>> providerDisplayNameDuplicate(ProviderDisplayNameDuplicateException ex) {
        log.warn("Provider display name duplicate: {}", ex.getMessage());
        return ResponseEntityFactory.conflict(ErrorCode.PROVIDER_DISPLAY_NAME_DUPLICATE.name(), ex.getMessage());
    }

    /**
     * Технические ошибки доменных операций --> 500.
     */
    @ExceptionHandler({
            CurrencyCreateException.class,
            CurrencyUpdateException.class,
            CurrencyDeleteException.class,
            FxSpotCreateException.class,
            FxSpotUpdateException.class,
            FxSpotDeleteException.class
    })
    public ResponseEntity<ApiResponse<Void>> domainTechnicalError(RuntimeException ex) {
        log.error("Domain operation failed: {}", ex.getMessage(), ex);
        return ResponseEntityFactory.error(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.UNEXPECTED_ERROR.name(), ex.getMessage());
    }
}
