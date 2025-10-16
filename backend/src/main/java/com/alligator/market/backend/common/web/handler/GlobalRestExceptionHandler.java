package com.alligator.market.backend.common.web.handler;

import com.alligator.market.backend.common.web.ApiResponse;
import com.alligator.market.backend.common.web.ResponseEntityFactory;
import com.alligator.market.domain.common.exception.NotFoundException;
import com.alligator.market.domain.common.exception.ResourceInUseException;
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
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Глобальный обработчик исключений REST-слоя.
 */
@Slf4j
@RestControllerAdvice
public class GlobalRestExceptionHandler {

    /** Ошибки валидации тела запроса (@Valid) 422. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        // Собираем все ошибки валидации в одно сообщение
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        log.warn("MethodArgumentNotValidException: {}", message);
        return ResponseEntityFactory.error(
                HttpStatus.UNPROCESSABLE_ENTITY,
                message
        );
    }

    /** Ошибки валидации параметров (@Validated) 400. */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException ex) {
        // Собираем все ошибки валидации в одно сообщение
        String message = ex.getConstraintViolations()
                .stream()
                .map(v -> v.getPropertyPath() + " " + v.getMessage())
                .collect(Collectors.joining("; "));

        log.warn("ConstraintViolationException: {}", message);
        return ResponseEntityFactory.error(
                HttpStatus.BAD_REQUEST,
                message
        );
    }

    /** Неверные аргументы запроса 400. */
    @ExceptionHandler({IllegalArgumentException.class, InstrumentNotSupportedException.class, HandlerNotFoundException.class})
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(RuntimeException ex) {
        log.warn("{}: {}", ex.getClass().getSimpleName(), ex.getMessage());
        return ResponseEntityFactory.error(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
    }

    /** Конфликт целостности данных 409. */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.warn("DataIntegrityViolationException: {}", ex.getMessage());
        return ResponseEntityFactory.error(
                HttpStatus.CONFLICT,
                "Resource already exists or violates integrity constraints"
        );
    }

    /** Конфликт бизнес-уникальности или использование ресурса 409. */
    @ExceptionHandler({
            CurrencyAlreadyExistsException.class,
            CurrencyNameDuplicateException.class,
            CurrencyUsedInFxSpotException.class,
            FxSpotAlreadyExistsException.class,
            ResourceInUseException.class
    })
    public ResponseEntity<ApiResponse<Void>> handleDomainConflict(RuntimeException ex) {
        log.warn("{}: {}", ex.getClass().getSimpleName(), ex.getMessage());
        return ResponseEntityFactory.error(
                HttpStatus.CONFLICT,
                ex.getMessage()
        );
    }

    /** Бизнес-валидация доменных моделей 400. */
    @ExceptionHandler(FxSpotSameCurrenciesException.class)
    public ResponseEntity<ApiResponse<Void>> handleDomainValidation(FxSpotSameCurrenciesException ex) {
        log.warn("FxSpotSameCurrenciesException: {}", ex.getMessage());
        return ResponseEntityFactory.error(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
    }

    /** Ресурс не найден 404. */
    @ExceptionHandler({NotFoundException.class, CurrencyNotFoundException.class, FxSpotNotFoundException.class})
    public ResponseEntity<ApiResponse<Void>> handleNotFound(RuntimeException ex) {
        log.warn("{}: {}", ex.getClass().getSimpleName(), ex.getMessage());
        return ResponseEntityFactory.notFound(ex.getMessage());
    }

    /** Технические ошибки доменных операций 500. */
    @ExceptionHandler({
            CurrencyCreateException.class,
            CurrencyUpdateException.class,
            CurrencyDeleteException.class,
            FxSpotCreateException.class,
            FxSpotUpdateException.class,
            FxSpotDeleteException.class,
            InstrumentWrongClassException.class
    })
    public ResponseEntity<ApiResponse<Void>> handleDomainFailures(RuntimeException ex) {
        log.error("{}: {}", ex.getClass().getSimpleName(), ex.getMessage(), ex);
        return ResponseEntityFactory.error(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage()
        );
    }

    /** Непредвиденные ошибки 500. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpected(Exception ex) {
        log.error("Unhandled exception", ex);
        return ResponseEntityFactory.error(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unexpected server error"
        );
    }
}
