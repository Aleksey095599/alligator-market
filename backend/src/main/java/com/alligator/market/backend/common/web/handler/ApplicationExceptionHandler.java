package com.alligator.market.backend.common.web.handler;

import com.alligator.market.backend.common.web.ApiResponse;
import com.alligator.market.backend.common.web.ResponseEntityFactory;
import com.alligator.market.domain.common.exception.NotFoundException;
import com.alligator.market.domain.common.exception.ResourceCreationException;
import com.alligator.market.domain.common.exception.ResourceInUseException;
import com.alligator.market.domain.common.exception.ResourceUpdateException;
import com.alligator.market.domain.instrument.type.forex.ref.currency.exception.CurrencyAlreadyExistsException;
import com.alligator.market.domain.instrument.type.forex.ref.currency.exception.CurrencyCreateException;
import com.alligator.market.domain.instrument.type.forex.ref.currency.exception.CurrencyDeleteException;
import com.alligator.market.domain.instrument.type.forex.ref.currency.exception.CurrencyNameDuplicateException;
import com.alligator.market.domain.instrument.type.forex.ref.currency.exception.CurrencyNotFoundException;
import com.alligator.market.domain.instrument.type.forex.ref.currency.exception.CurrencyUpdateException;
import com.alligator.market.domain.instrument.type.forex.ref.currency.exception.CurrencyUsedInFxSpotException;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxSpotAlreadyExistsException;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxSpotCreateException;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxSpotDeleteException;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxSpotNotFoundException;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxSpotSameCurrenciesException;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxSpotUpdateException;
import com.alligator.market.domain.provider.exception.HandlerNotFoundException;
import com.alligator.market.domain.provider.exception.InstrumentNotSupportedException;
import com.alligator.market.domain.provider.exception.InstrumentWrongClassException;
import com.alligator.market.domain.provider.exception.ProviderCodeDuplicateException;
import com.alligator.market.domain.provider.exception.ProviderDisplayNameDuplicateException;
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

    /** Валюта с таким кодом уже существует. */
    @ExceptionHandler(CurrencyAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleCurrencyAlreadyExists(CurrencyAlreadyExistsException ex) {
        return warn(HttpStatus.CONFLICT, ex);
    }

    /** Валюта с таким именем уже существует. */
    @ExceptionHandler(CurrencyNameDuplicateException.class)
    public ResponseEntity<ApiResponse<Void>> handleCurrencyNameDuplicate(CurrencyNameDuplicateException ex) {
        return warn(HttpStatus.CONFLICT, ex);
    }

    /** Валюта используется в инструментах FX_SPOT. */
    @ExceptionHandler(CurrencyUsedInFxSpotException.class)
    public ResponseEntity<ApiResponse<Void>> handleCurrencyUsedInFxSpot(CurrencyUsedInFxSpotException ex) {
        return warn(HttpStatus.CONFLICT, ex);
    }

    /** Ошибка создания валюты. */
    @ExceptionHandler(CurrencyCreateException.class)
    public ResponseEntity<ApiResponse<Void>> handleCurrencyCreate(CurrencyCreateException ex) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    /** Ошибка обновления валюты. */
    @ExceptionHandler(CurrencyUpdateException.class)
    public ResponseEntity<ApiResponse<Void>> handleCurrencyUpdate(CurrencyUpdateException ex) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    /** Ошибка удаления валюты. */
    @ExceptionHandler(CurrencyDeleteException.class)
    public ResponseEntity<ApiResponse<Void>> handleCurrencyDelete(CurrencyDeleteException ex) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    /** Валюта не найдена. */
    @ExceptionHandler(CurrencyNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleCurrencyNotFound(CurrencyNotFoundException ex) {
        return notFound(ex);
    }

    /** Дублирование кода провайдера. */
    @ExceptionHandler(ProviderCodeDuplicateException.class)
    public ResponseEntity<ApiResponse<Void>> handleProviderCodeDuplicate(ProviderCodeDuplicateException ex) {
        return warn(HttpStatus.CONFLICT, ex);
    }

    /** Дублирование отображаемого имени провайдера. */
    @ExceptionHandler(ProviderDisplayNameDuplicateException.class)
    public ResponseEntity<ApiResponse<Void>> handleProviderDisplayNameDuplicate(ProviderDisplayNameDuplicateException ex) {
        return warn(HttpStatus.CONFLICT, ex);
    }

    /** Ресурс используется. */
    @ExceptionHandler(ResourceInUseException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceInUse(ResourceInUseException ex) {
        return warn(HttpStatus.CONFLICT, ex);
    }

    /** Ошибка создания ресурса. */
    @ExceptionHandler(ResourceCreationException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceCreation(ResourceCreationException ex) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    /** Ошибка обновления ресурса. */
    @ExceptionHandler(ResourceUpdateException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceUpdate(ResourceUpdateException ex) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    /** Сущность не найдена. */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(NotFoundException ex) {
        return notFound(ex);
    }

    /** Инструмент FX_SPOT уже существует. */
    @ExceptionHandler(FxSpotAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleFxSpotAlreadyExists(FxSpotAlreadyExistsException ex) {
        return warn(HttpStatus.CONFLICT, ex);
    }

    /** Ошибка создания инструмента FX_SPOT. */
    @ExceptionHandler(FxSpotCreateException.class)
    public ResponseEntity<ApiResponse<Void>> handleFxSpotCreate(FxSpotCreateException ex) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    /** Ошибка обновления инструмента FX_SPOT. */
    @ExceptionHandler(FxSpotUpdateException.class)
    public ResponseEntity<ApiResponse<Void>> handleFxSpotUpdate(FxSpotUpdateException ex) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    /** Ошибка удаления инструмента FX_SPOT. */
    @ExceptionHandler(FxSpotDeleteException.class)
    public ResponseEntity<ApiResponse<Void>> handleFxSpotDelete(FxSpotDeleteException ex) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    /** Инструмент FX_SPOT не найден. */
    @ExceptionHandler(FxSpotNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleFxSpotNotFound(FxSpotNotFoundException ex) {
        return notFound(ex);
    }

    /** Базовая и котируемая валюты совпадают. */
    @ExceptionHandler(FxSpotSameCurrenciesException.class)
    public ResponseEntity<ApiResponse<Void>> handleFxSpotSameCurrencies(FxSpotSameCurrenciesException ex) {
        return warn(HttpStatus.BAD_REQUEST, ex);
    }

    /** Тип инструмента не поддерживается провайдером. */
    @ExceptionHandler(InstrumentNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleInstrumentNotSupported(InstrumentNotSupportedException ex) {
        return warn(HttpStatus.BAD_REQUEST, ex);
    }

    /** Обработчик для инструмента не найден. */
    @ExceptionHandler(HandlerNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleHandlerNotFound(HandlerNotFoundException ex) {
        return warn(HttpStatus.BAD_REQUEST, ex);
    }

    /** Класс инструмента не соответствует ожиданиям обработчика. */
    @ExceptionHandler(InstrumentWrongClassException.class)
    public ResponseEntity<ApiResponse<Void>> handleInstrumentWrongClass(InstrumentWrongClassException ex) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    /**
     * Формирует ответ с уровнем логирования WARN.
     *
     * @param status HTTP-статус
     * @param ex     доменное исключение
     * @return готовый HTTP-ответ
     */
    private ResponseEntity<ApiResponse<Void>> warn(HttpStatus status, RuntimeException ex) {
        log.warn("{}: {}", ex.getClass().getSimpleName(), ex.getMessage());
        return ResponseEntityFactory.error(status, ex.getMessage());
    }

    /**
     * Формирует ответ 404 Not Found с уровнем WARN.
     *
     * @param ex доменное исключение
     * @return готовый HTTP-ответ
     */
    private ResponseEntity<ApiResponse<Void>> notFound(RuntimeException ex) {
        log.warn("{}: {}", ex.getClass().getSimpleName(), ex.getMessage());
        return ResponseEntityFactory.notFound(ex.getMessage());
    }

    /**
     * Формирует ответ с уровнем логирования ERROR и стеком исключения.
     *
     * @param status HTTP-статус
     * @param ex     доменное исключение
     * @return готовый HTTP-ответ
     */
    private ResponseEntity<ApiResponse<Void>> error(HttpStatus status, RuntimeException ex) {
        log.error("{}: {}", ex.getClass().getSimpleName(), ex.getMessage(), ex);
        return ResponseEntityFactory.error(status, ex.getMessage());
    }
}
