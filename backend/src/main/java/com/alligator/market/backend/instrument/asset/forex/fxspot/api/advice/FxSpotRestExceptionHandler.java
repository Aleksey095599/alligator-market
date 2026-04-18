package com.alligator.market.backend.instrument.asset.forex.fxspot.api.advice;

import com.alligator.market.backend.common.web.response.ApiResponse;
import com.alligator.market.backend.common.web.response.ResponseEntityFactory;
import com.alligator.market.backend.instrument.asset.forex.fxspot.api.command.create.controller.CreateFxSpotController;
import com.alligator.market.backend.instrument.asset.forex.fxspot.api.command.delete.controller.DeleteFxSpotController;
import com.alligator.market.backend.instrument.asset.forex.fxspot.api.command.update.controller.UpdateFxSpotController;
import com.alligator.market.backend.instrument.asset.forex.fxspot.api.query.list.controller.ListFxSpotsController;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.exception.CurrencyNotFoundException;
import com.alligator.market.domain.common.exception.DomainErrorCode;
import com.alligator.market.domain.instrument.asset.forex.fxspot.exception.FxSpotAlreadyExistsException;
import com.alligator.market.domain.instrument.asset.forex.fxspot.exception.FxSpotCreateException;
import com.alligator.market.domain.instrument.asset.forex.fxspot.exception.FxSpotDeleteException;
import com.alligator.market.domain.instrument.asset.forex.fxspot.exception.FxSpotNotFoundException;
import com.alligator.market.domain.instrument.asset.forex.fxspot.exception.FxSpotSameCurrenciesException;
import com.alligator.market.domain.instrument.asset.forex.fxspot.exception.FxSpotUpdateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Feature-specific обработчик ошибок API для FOREX_SPOT.
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(assignableTypes = {
        CreateFxSpotController.class,
        UpdateFxSpotController.class,
        DeleteFxSpotController.class,
        ListFxSpotsController.class
})
public class FxSpotRestExceptionHandler {

    @ExceptionHandler(FxSpotAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> fxSpotAlreadyExists(FxSpotAlreadyExistsException ex) {
        log.warn("FX Spot already exists: {}", ex.getMessage());
        return ResponseEntityFactory.conflict(DomainErrorCode.FX_SPOT_ALREADY_EXISTS.name(), ex.getMessage());
    }

    @ExceptionHandler(FxSpotNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> fxSpotNotFound(FxSpotNotFoundException ex) {
        log.warn("FX Spot not found: {}", ex.getMessage());
        return ResponseEntityFactory.notFound(DomainErrorCode.FX_SPOT_NOT_FOUND.name(), ex.getMessage());
    }

    @ExceptionHandler(FxSpotSameCurrenciesException.class)
    public ResponseEntity<ApiResponse<Void>> fxSpotSameCurrencies(FxSpotSameCurrenciesException ex) {
        log.warn("FX Spot same currencies: {}", ex.getMessage());
        return ResponseEntityFactory.badRequest(DomainErrorCode.FX_SPOT_SAME_CURRENCIES.name(), ex.getMessage());
    }

    @ExceptionHandler(CurrencyNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> currencyNotFound(CurrencyNotFoundException ex) {
        log.warn("Currency not found: {}", ex.getMessage());
        return ResponseEntityFactory.notFound(DomainErrorCode.CURRENCY_NOT_FOUND.name(), ex.getMessage());
    }

    @ExceptionHandler(FxSpotCreateException.class)
    public ResponseEntity<ApiResponse<Void>> fxSpotCreateFailed(FxSpotCreateException ex) {
        log.error("FX Spot create failed: {}", ex.getMessage(), ex);
        return ResponseEntityFactory.error(
                HttpStatus.INTERNAL_SERVER_ERROR,
                DomainErrorCode.FX_SPOT_CREATE_FAILED.name(),
                ex.getMessage()
        );
    }

    @ExceptionHandler(FxSpotUpdateException.class)
    public ResponseEntity<ApiResponse<Void>> fxSpotUpdateFailed(FxSpotUpdateException ex) {
        log.error("FX Spot update failed: {}", ex.getMessage(), ex);
        return ResponseEntityFactory.error(
                HttpStatus.INTERNAL_SERVER_ERROR,
                DomainErrorCode.FX_SPOT_UPDATE_FAILED.name(),
                ex.getMessage()
        );
    }

    @ExceptionHandler(FxSpotDeleteException.class)
    public ResponseEntity<ApiResponse<Void>> fxSpotDeleteFailed(FxSpotDeleteException ex) {
        log.error("FX Spot delete failed: {}", ex.getMessage(), ex);
        return ResponseEntityFactory.error(
                HttpStatus.INTERNAL_SERVER_ERROR,
                DomainErrorCode.FX_SPOT_DELETE_FAILED.name(),
                ex.getMessage()
        );
    }
}
