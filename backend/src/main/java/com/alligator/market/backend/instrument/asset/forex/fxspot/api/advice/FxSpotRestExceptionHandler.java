package com.alligator.market.backend.instrument.asset.forex.fxspot.api.advice;

import com.alligator.market.backend.instrument.asset.forex.fxspot.api.command.create.controller.CreateFxSpotController;
import com.alligator.market.backend.instrument.asset.forex.fxspot.api.command.delete.controller.DeleteFxSpotController;
import com.alligator.market.backend.instrument.asset.forex.fxspot.api.command.update.controller.UpdateFxSpotController;
import com.alligator.market.backend.instrument.asset.forex.fxspot.api.query.list.controller.FxSpotListController;
import com.alligator.market.backend.instrument.asset.forex.fxspot.application.exception.FxSpotAlreadyExistsException;
import com.alligator.market.backend.instrument.asset.forex.fxspot.application.exception.FxSpotCreateException;
import com.alligator.market.backend.instrument.asset.forex.fxspot.application.exception.FxSpotDeleteException;
import com.alligator.market.backend.instrument.asset.forex.fxspot.application.exception.FxSpotInUseException;
import com.alligator.market.backend.instrument.asset.forex.fxspot.application.exception.FxSpotNotFoundException;
import com.alligator.market.backend.instrument.asset.forex.fxspot.application.exception.FxSpotUpdateException;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.exception.CurrencyNotFoundException;
import com.alligator.market.domain.instrument.asset.forex.fxspot.exception.FxSpotSameCurrenciesException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
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
        FxSpotListController.class
})
public class FxSpotRestExceptionHandler {

    @ExceptionHandler(FxSpotAlreadyExistsException.class)
    public ProblemDetail fxSpotAlreadyExists(FxSpotAlreadyExistsException ex) {
        log.warn("FX Spot already exists: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.CONFLICT,
                "FX Spot already exists",
                ex.getMessage(),
                FxSpotApiErrorCode.FX_SPOT_ALREADY_EXISTS.name()
        );
    }

    @ExceptionHandler(FxSpotNotFoundException.class)
    public ProblemDetail fxSpotNotFound(FxSpotNotFoundException ex) {
        log.warn("FX Spot not found: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.NOT_FOUND,
                "FX Spot not found",
                ex.getMessage(),
                FxSpotApiErrorCode.FX_SPOT_NOT_FOUND.name()
        );
    }

    @ExceptionHandler(FxSpotSameCurrenciesException.class)
    public ProblemDetail fxSpotSameCurrencies(FxSpotSameCurrenciesException ex) {
        log.warn("FX Spot same currencies: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "FX Spot same currencies",
                ex.getMessage(),
                FxSpotApiErrorCode.FX_SPOT_SAME_CURRENCIES.name()
        );
    }

    @ExceptionHandler(FxSpotInUseException.class)
    public ProblemDetail fxSpotInUse(FxSpotInUseException ex) {
        log.warn("FX Spot is in use: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.CONFLICT,
                "FX Spot is in use",
                ex.getMessage(),
                FxSpotApiErrorCode.FX_SPOT_IN_USE.name()
        );
    }

    @ExceptionHandler(CurrencyNotFoundException.class)
    public ProblemDetail currencyNotFound(CurrencyNotFoundException ex) {
        log.warn("Currency not found: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.NOT_FOUND,
                "Currency not found",
                ex.getMessage(),
                FxSpotApiErrorCode.CURRENCY_NOT_FOUND.name()
        );
    }

    @ExceptionHandler({
            FxSpotCreateException.class,
            FxSpotUpdateException.class,
            FxSpotDeleteException.class
    })
    public ProblemDetail fxSpotTechnicalError(RuntimeException ex) {
        log.error("FX Spot operation failed: {}", ex.getMessage(), ex);

        FxSpotApiErrorCode errorCode = resolveTechnicalErrorCode(ex);
        String title = resolveTechnicalErrorTitle(ex);

        return buildProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                title,
                ex.getMessage(),
                errorCode.name()
        );
    }

    /* Подбираем код ошибки для технических application-исключений FX_SPOT. */
    private FxSpotApiErrorCode resolveTechnicalErrorCode(RuntimeException ex) {
        if (ex instanceof FxSpotCreateException) {
            return FxSpotApiErrorCode.FX_SPOT_CREATE_FAILED;
        }
        if (ex instanceof FxSpotUpdateException) {
            return FxSpotApiErrorCode.FX_SPOT_UPDATE_FAILED;
        }
        return FxSpotApiErrorCode.FX_SPOT_DELETE_FAILED;
    }

    /* Подбираем title для технических application-исключений FX_SPOT. */
    private String resolveTechnicalErrorTitle(RuntimeException ex) {
        if (ex instanceof FxSpotCreateException) {
            return "FX Spot create failed";
        }
        if (ex instanceof FxSpotUpdateException) {
            return "FX Spot update failed";
        }
        return "FX Spot delete failed";
    }

    /* Формируем стандартный ProblemDetail ответ с дополнительным errorCode. */
    private ProblemDetail buildProblemDetail(
            HttpStatus status,
            String title,
            String detail,
            String errorCode
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        problemDetail.setProperty("errorCode", errorCode);
        return problemDetail;
    }
}
