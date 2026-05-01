package com.alligator.market.backend.sourcing.plan.api.advice;

import com.alligator.market.backend.sourcing.plan.api.command.create.controller.CreateInstrumentSourcePlanController;
import com.alligator.market.backend.sourcing.plan.api.command.delete.controller.DeleteInstrumentSourcePlanController;
import com.alligator.market.backend.sourcing.plan.api.command.replace.controller.ReplaceInstrumentSourcePlanController;
import com.alligator.market.backend.sourcing.plan.api.query.get.controller.GetInstrumentSourcePlanController;
import com.alligator.market.backend.sourcing.plan.api.query.list.controller.InstrumentSourcePlanListController;
import com.alligator.market.backend.sourcing.plan.api.query.options.controller.InstrumentSourcePlanOptionsQueryController;
import com.alligator.market.backend.sourcing.plan.application.exception.InstrumentCodeNotFoundException;
import com.alligator.market.backend.sourcing.plan.application.exception.InstrumentSourcePlanAlreadyExistsException;
import com.alligator.market.backend.sourcing.plan.application.exception.InstrumentSourcePlanNotFoundException;
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
        CreateInstrumentSourcePlanController.class,
        DeleteInstrumentSourcePlanController.class,
        ReplaceInstrumentSourcePlanController.class,
        GetInstrumentSourcePlanController.class,
        InstrumentSourcePlanListController.class,
        InstrumentSourcePlanOptionsQueryController.class
})
public class InstrumentSourcePlanApiExceptionHandler {


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
                InstrumentSourcePlanApiErrorCode.INSTRUMENT_CODE_NOT_FOUND.code()
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
                InstrumentSourcePlanApiErrorCode.PROVIDER_CODES_NOT_FOUND.code()
        );
    }

    /**
     * План источников уже существует --> 409.
     */
    @ExceptionHandler(InstrumentSourcePlanAlreadyExistsException.class)
    public ProblemDetail handleInstrumentSourcePlanAlreadyExists(
            InstrumentSourcePlanAlreadyExistsException ex
    ) {
        log.warn("Instrument source plan already exists: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.CONFLICT,
                "Instrument source plan already exists",
                ex.getMessage(),
                InstrumentSourcePlanApiErrorCode.INSTRUMENT_SOURCE_PLAN_ALREADY_EXISTS.code()
        );
    }

    /**
     * План источников не найден --> 404.
     */
    @ExceptionHandler(InstrumentSourcePlanNotFoundException.class)
    public ProblemDetail handleInstrumentSourcePlanNotFound(InstrumentSourcePlanNotFoundException ex) {
        log.warn("Instrument source plan not found: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.NOT_FOUND,
                "Instrument source plan not found",
                ex.getMessage(),
                InstrumentSourcePlanApiErrorCode.INSTRUMENT_SOURCE_PLAN_NOT_FOUND.code()
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
