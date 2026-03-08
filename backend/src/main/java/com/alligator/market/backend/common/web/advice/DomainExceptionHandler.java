package com.alligator.market.backend.common.web.advice;

import com.alligator.market.backend.common.web.response.ApiResponse;
import com.alligator.market.backend.common.web.response.ResponseEntityFactory;
import com.alligator.market.domain.common.exception.DomainErrorCode;
import com.alligator.market.domain.instrument.asset.forex.contract.spot.exception.*;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.exception.*;
import com.alligator.market.domain.marketdata.provider.model.handler.exception.HandlerNotFoundException;
import com.alligator.market.domain.marketdata.provider.model.handler.exception.InstrumentNotSupportedException;
import com.alligator.market.domain.marketdata.provider.model.handler.exception.InstrumentWrongClassException;
import com.alligator.market.domain.marketdata.provider.model.handler.exception.InstrumentWrongAssetClassException;
import com.alligator.market.domain.marketdata.provider.model.handler.exception.InstrumentWrongContractTypeException;
import com.alligator.market.domain.marketdata.provider.registry.exception.ProviderCodeDuplicateException;
import com.alligator.market.domain.marketdata.provider.registry.exception.ProviderDisplayNameDuplicateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Централизованный обработчик исключений, вызванных нарушением доменной логики приложения.
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
     * Валюта используется в инструментах FOREX_SPOT --> 409.
     */
    @ExceptionHandler(CurrencyUsedInFxSpotException.class)
    public ResponseEntity<ApiResponse<Void>> currencyUsedInFxSpot(CurrencyUsedInFxSpotException ex) {
        log.warn("Currency used in FX Spot: {}", ex.getMessage());
        return ResponseEntityFactory.conflict(DomainErrorCode.CURRENCY_USED_IN_FX_SPOT.name(), ex.getMessage());
    }

    /**
     * Валюта не найдена --> 404.
     */
    @ExceptionHandler(CurrencyNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> currencyNotFound(CurrencyNotFoundException ex) {
        log.warn("Currency not found: {}", ex.getMessage());
        return ResponseEntityFactory.notFound(DomainErrorCode.CURRENCY_NOT_FOUND.name(), ex.getMessage());
    }

    /**
     * Валюта уже используется при создании FOREX_SPOT --> 409.
     */
    @ExceptionHandler(FxSpotAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> fxSpotAlreadyExists(FxSpotAlreadyExistsException ex) {
        log.warn("FX Spot already exists: {}", ex.getMessage());
        return ResponseEntityFactory.conflict(DomainErrorCode.FX_SPOT_ALREADY_EXISTS.name(), ex.getMessage());
    }

    /**
     * Инструмент FOREX_SPOT не найден --> 404.
     */
    @ExceptionHandler(FxSpotNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> fxSpotNotFound(FxSpotNotFoundException ex) {
        log.warn("FX Spot not found: {}", ex.getMessage());
        return ResponseEntityFactory.notFound(DomainErrorCode.FX_SPOT_NOT_FOUND.name(), ex.getMessage());
    }

    /**
     * Указаны одинаковые валюты для FOREX_SPOT --> 400.
     */
    @ExceptionHandler(FxSpotSameCurrenciesException.class)
    public ResponseEntity<ApiResponse<Void>> fxSpotSameCurrencies(FxSpotSameCurrenciesException ex) {
        log.warn("FX Spot same currencies: {}", ex.getMessage());
        return ResponseEntityFactory.badRequest(DomainErrorCode.FX_SPOT_SAME_CURRENCIES.name(), ex.getMessage());
    }

    /**
     * Инструмент не поддерживается провайдером --> 400.
     */
    @ExceptionHandler(InstrumentNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> instrumentNotSupported(InstrumentNotSupportedException ex) {
        log.warn("Instrument not supported: {}", ex.getMessage());
        return ResponseEntityFactory.badRequest(DomainErrorCode.INSTRUMENT_NOT_SUPPORTED.name(), ex.getMessage());
    }

    /**
     * Обработчик инструмента не найден --> 404.
     */
    @ExceptionHandler(HandlerNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handlerNotFound(HandlerNotFoundException ex) {
        log.warn("Handler not found: {}", ex.getMessage());
        return ResponseEntityFactory.notFound(DomainErrorCode.HANDLER_NOT_FOUND.name(), ex.getMessage());
    }

    /**
     * Некорректный класс актива, тип контракта или java-класс инструмента --> 400.
     */
    @ExceptionHandler({
            InstrumentWrongClassException.class,
            InstrumentWrongAssetClassException.class,
            InstrumentWrongContractTypeException.class
    })
    public ResponseEntity<ApiResponse<Void>> instrumentWrong(RuntimeException ex) {
        log.warn("Instrument mismatch: {}", ex.getMessage());
        String code;
        if (ex instanceof InstrumentWrongClassException) {
            code = DomainErrorCode.INSTRUMENT_WRONG_CLASS.name();
        } else if (ex instanceof InstrumentWrongAssetClassException) {
            code = DomainErrorCode.INSTRUMENT_WRONG_ASSET_CLASS.name();
        } else {
            code = DomainErrorCode.INSTRUMENT_WRONG_CONTRACT_TYPE.name();
        }
        return ResponseEntityFactory.badRequest(code, ex.getMessage());
    }

    /**
     * Дублирование кода провайдера --> 409.
     */
    @ExceptionHandler(ProviderCodeDuplicateException.class)
    public ResponseEntity<ApiResponse<Void>> providerCodeDuplicate(ProviderCodeDuplicateException ex) {
        log.warn("Provider code duplicate: {}", ex.getMessage());
        return ResponseEntityFactory.conflict(DomainErrorCode.PROVIDER_CODE_DUPLICATE.name(), ex.getMessage());
    }

    /**
     * Дублирование отображаемого имени провайдера --> 409.
     */
    @ExceptionHandler(ProviderDisplayNameDuplicateException.class)
    public ResponseEntity<ApiResponse<Void>> providerDisplayNameDuplicate(ProviderDisplayNameDuplicateException ex) {
        log.warn("Provider display name duplicate: {}", ex.getMessage());
        return ResponseEntityFactory.conflict(DomainErrorCode.PROVIDER_DISPLAY_NAME_DUPLICATE.name(), ex.getMessage());
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
        String errorCode = resolveTechnicalErrorCode(ex);
        return ResponseEntityFactory.error(HttpStatus.INTERNAL_SERVER_ERROR, errorCode, ex.getMessage());
    }

    /* Подбираем код ошибки для технических доменных исключений. */
    private String resolveTechnicalErrorCode(RuntimeException ex) {
        if (ex instanceof CurrencyCreateException) {
            return DomainErrorCode.CURRENCY_CREATE_FAILED.name();
        }
        if (ex instanceof CurrencyUpdateException) {
            return DomainErrorCode.CURRENCY_UPDATE_FAILED.name();
        }
        if (ex instanceof CurrencyDeleteException) {
            return DomainErrorCode.CURRENCY_DELETE_FAILED.name();
        }
        if (ex instanceof FxSpotCreateException) {
            return DomainErrorCode.FX_SPOT_CREATE_FAILED.name();
        }
        if (ex instanceof FxSpotUpdateException) {
            return DomainErrorCode.FX_SPOT_UPDATE_FAILED.name();
        }
        return DomainErrorCode.FX_SPOT_DELETE_FAILED.name();
    }
}
