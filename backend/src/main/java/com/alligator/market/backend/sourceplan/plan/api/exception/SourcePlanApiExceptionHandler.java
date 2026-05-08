package com.alligator.market.backend.sourceplan.plan.api.exception;

import com.alligator.market.backend.sourceplan.plan.api.command.create.controller.CreateSourcePlanController;
import com.alligator.market.backend.sourceplan.plan.api.command.delete.controller.DeleteSourcePlanController;
import com.alligator.market.backend.sourceplan.plan.api.command.replace.controller.ReplaceSourcePlanController;
import com.alligator.market.backend.sourceplan.plan.api.query.get.controller.GetSourcePlanController;
import com.alligator.market.backend.sourceplan.plan.api.query.list.controller.SourcePlanListController;
import com.alligator.market.backend.sourceplan.plan.api.query.options.controller.SourcePlanOptionsQueryController;
import com.alligator.market.backend.sourceplan.plan.application.exception.MarketDataCapturerCodeNotFoundException;
import com.alligator.market.backend.sourceplan.plan.application.exception.InstrumentCodeNotFoundException;
import com.alligator.market.backend.sourceplan.plan.application.exception.SourcePlanAlreadyExistsException;
import com.alligator.market.backend.sourceplan.plan.application.exception.SourcePlanNotFoundException;
import com.alligator.market.backend.sourceplan.plan.application.exception.MarketDataSourceCodesNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Feature-specific API exception handler.
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(assignableTypes = {
        CreateSourcePlanController.class,
        DeleteSourcePlanController.class,
        ReplaceSourcePlanController.class,
        GetSourcePlanController.class,
        SourcePlanListController.class,
        SourcePlanOptionsQueryController.class
})
public class SourcePlanApiExceptionHandler {


    /**
     * Capturer code is missing in the passport projection --> 400.
     */
    @ExceptionHandler(MarketDataCapturerCodeNotFoundException.class)
    public ProblemDetail handleMarketDataCapturerCodeNotFound(MarketDataCapturerCodeNotFoundException ex) {
        log.warn("Capturer code does not exist: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Capturer code not found",
                ex.getMessage(),
                SourcePlanApiErrorCode.CAPTURER_CODE_NOT_FOUND.code()
        );
    }

    /**
     * Instrument code is missing in the registry --> 400.
     */
    @ExceptionHandler(InstrumentCodeNotFoundException.class)
    public ProblemDetail handleInstrumentCodeNotFound(InstrumentCodeNotFoundException ex) {
        log.warn("Instrument code does not exist: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Instrument code not found",
                ex.getMessage(),
                SourcePlanApiErrorCode.INSTRUMENT_CODE_NOT_FOUND.code()
        );
    }

    /**
     * One or more source codes are missing in the registry --> 400.
     */
    @ExceptionHandler(MarketDataSourceCodesNotFoundException.class)
    public ProblemDetail handleMarketDataSourceCodesNotFound(MarketDataSourceCodesNotFoundException ex) {
        log.warn("Market data source codes do not exist: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Market data source codes not found",
                ex.getMessage(),
                SourcePlanApiErrorCode.MARKET_DATA_SOURCE_CODES_NOT_FOUND.code()
        );
    }

    /**
     * Source plan already exists --> 409.
     */
    @ExceptionHandler(SourcePlanAlreadyExistsException.class)
    public ProblemDetail handleSourcePlanAlreadyExists(
            SourcePlanAlreadyExistsException ex
    ) {
        log.warn("Source plan already exists: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.CONFLICT,
                "Source plan already exists",
                ex.getMessage(),
                SourcePlanApiErrorCode.SOURCE_PLAN_ALREADY_EXISTS.code()
        );
    }

    /**
     * Source plan was not found --> 404.
     */
    @ExceptionHandler(SourcePlanNotFoundException.class)
    public ProblemDetail handleSourcePlanNotFound(SourcePlanNotFoundException ex) {
        log.warn("Source plan not found: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.NOT_FOUND,
                "Source plan not found",
                ex.getMessage(),
                SourcePlanApiErrorCode.SOURCE_PLAN_NOT_FOUND.code()
        );
    }

    /* Shared ProblemDetail builder for this feature's error contract. */
    private ProblemDetail buildProblemDetail(HttpStatus status, String title, String detail, String errorCode) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        problemDetail.setProperty("errorCode", errorCode);
        return problemDetail;
    }
}
