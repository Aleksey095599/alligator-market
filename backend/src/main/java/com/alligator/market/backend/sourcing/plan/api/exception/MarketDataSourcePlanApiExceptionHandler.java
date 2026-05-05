package com.alligator.market.backend.sourcing.plan.api.exception;

import com.alligator.market.backend.sourcing.plan.api.command.create.controller.CreateMarketDataSourcePlanController;
import com.alligator.market.backend.sourcing.plan.api.command.delete.controller.DeleteMarketDataSourcePlanController;
import com.alligator.market.backend.sourcing.plan.api.command.replace.controller.ReplaceMarketDataSourcePlanController;
import com.alligator.market.backend.sourcing.plan.api.query.get.controller.GetMarketDataSourcePlanController;
import com.alligator.market.backend.sourcing.plan.api.query.list.controller.MarketDataSourcePlanListController;
import com.alligator.market.backend.sourcing.plan.api.query.options.controller.MarketDataSourcePlanOptionsQueryController;
import com.alligator.market.backend.sourcing.plan.application.exception.MarketDataCaptureProcessCodeNotFoundException;
import com.alligator.market.backend.sourcing.plan.application.exception.InstrumentCodeNotFoundException;
import com.alligator.market.backend.sourcing.plan.application.exception.MarketDataSourcePlanAlreadyExistsException;
import com.alligator.market.backend.sourcing.plan.application.exception.MarketDataSourcePlanNotFoundException;
import com.alligator.market.backend.sourcing.plan.application.exception.ProviderCodesNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Feature-specific обработчик ошибок API.
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(assignableTypes = {
        CreateMarketDataSourcePlanController.class,
        DeleteMarketDataSourcePlanController.class,
        ReplaceMarketDataSourcePlanController.class,
        GetMarketDataSourcePlanController.class,
        MarketDataSourcePlanListController.class,
        MarketDataSourcePlanOptionsQueryController.class
})
public class MarketDataSourcePlanApiExceptionHandler {


    /**
     * Код процесса захвата отсутствует в passport projection --> 400.
     */
    @ExceptionHandler(MarketDataCaptureProcessCodeNotFoundException.class)
    public ProblemDetail handleMarketDataCaptureProcessCodeNotFound(MarketDataCaptureProcessCodeNotFoundException ex) {
        log.warn("Capture process code does not exist: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Capture process code not found",
                ex.getMessage(),
                MarketDataSourcePlanApiErrorCode.CAPTURE_PROCESS_CODE_NOT_FOUND.code()
        );
    }

    /**
     * Код инструмента отсутствует в registry --> 400.
     */
    @ExceptionHandler(InstrumentCodeNotFoundException.class)
    public ProblemDetail handleInstrumentCodeNotFound(InstrumentCodeNotFoundException ex) {
        log.warn("Instrument code does not exist: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Instrument code not found",
                ex.getMessage(),
                MarketDataSourcePlanApiErrorCode.INSTRUMENT_CODE_NOT_FOUND.code()
        );
    }

    /**
     * Один или несколько кодов провайдера отсутствуют в registry --> 400.
     */
    @ExceptionHandler(ProviderCodesNotFoundException.class)
    public ProblemDetail handleProviderCodesNotFound(ProviderCodesNotFoundException ex) {
        log.warn("Provider codes do not exist: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Provider codes not found",
                ex.getMessage(),
                MarketDataSourcePlanApiErrorCode.PROVIDER_CODES_NOT_FOUND.code()
        );
    }

    /**
     * План источников уже существует --> 409.
     */
    @ExceptionHandler(MarketDataSourcePlanAlreadyExistsException.class)
    public ProblemDetail handleMarketDataSourcePlanAlreadyExists(
            MarketDataSourcePlanAlreadyExistsException ex
    ) {
        log.warn("Market data source plan already exists: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.CONFLICT,
                "Market data source plan already exists",
                ex.getMessage(),
                MarketDataSourcePlanApiErrorCode.MARKET_DATA_SOURCE_PLAN_ALREADY_EXISTS.code()
        );
    }

    /**
     * План источников не найден --> 404.
     */
    @ExceptionHandler(MarketDataSourcePlanNotFoundException.class)
    public ProblemDetail handleMarketDataSourcePlanNotFound(MarketDataSourcePlanNotFoundException ex) {
        log.warn("Market data source plan not found: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.NOT_FOUND,
                "Market data source plan not found",
                ex.getMessage(),
                MarketDataSourcePlanApiErrorCode.MARKET_DATA_SOURCE_PLAN_NOT_FOUND.code()
        );
    }

    /* Общий builder ProblemDetail для единообразного контракта ошибок feature. */
    private ProblemDetail buildProblemDetail(HttpStatus status, String title, String detail, String errorCode) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        problemDetail.setProperty("errorCode", errorCode);
        return problemDetail;
    }
}
