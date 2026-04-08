package com.alligator.market.backend.sourcing.plan.api.advice;

import com.alligator.market.backend.common.web.response.ApiResponse;
import com.alligator.market.backend.common.web.response.ResponseEntityFactory;
import com.alligator.market.backend.sourcing.plan.api.command.create.controller.CreateInstrumentSourcePlanController;
import com.alligator.market.backend.sourcing.plan.api.command.delete.controller.DeleteInstrumentSourcePlanController;
import com.alligator.market.backend.sourcing.plan.api.query.get.controller.GetInstrumentSourcePlanController;
import com.alligator.market.backend.sourcing.plan.api.query.options.controller.InstrumentSourcePlanOptionsQueryController;
import com.alligator.market.backend.sourcing.plan.application.command.create.exception.InstrumentCodeNotFoundException;
import com.alligator.market.backend.sourcing.plan.application.command.create.exception.ProviderCodesNotFoundException;
import com.alligator.market.backend.sourcing.plan.application.exception.InstrumentSourcePlanAlreadyExistsException;
import com.alligator.market.backend.sourcing.plan.application.exception.InstrumentSourcePlanNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Feature-specific обработчик ошибок API для сценариев InstrumentSourcePlan.
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(assignableTypes = {
        CreateInstrumentSourcePlanController.class,
        DeleteInstrumentSourcePlanController.class,
        GetInstrumentSourcePlanController.class,
        InstrumentSourcePlanOptionsQueryController.class
})
public class InstrumentSourcePlanRestExceptionHandler {

    /* Единые коды ошибок sourcing feature. */
    private static final String INSTRUMENT_CODE_NOT_FOUND = "INSTRUMENT_CODE_NOT_FOUND";
    private static final String PROVIDER_CODES_NOT_FOUND = "PROVIDER_CODES_NOT_FOUND";
    private static final String INSTRUMENT_SOURCE_PLAN_ALREADY_EXISTS = "INSTRUMENT_SOURCE_PLAN_ALREADY_EXISTS";
    private static final String INSTRUMENT_SOURCE_PLAN_NOT_FOUND = "INSTRUMENT_SOURCE_PLAN_NOT_FOUND";

    /**
     * Код инструмента отсутствует в registry --> 400.
     */
    @ExceptionHandler(InstrumentCodeNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleInstrumentCodeNotFound(InstrumentCodeNotFoundException ex) {
        log.warn("Instrument code does not exist: {}", ex.getMessage());
        return ResponseEntityFactory.badRequest(INSTRUMENT_CODE_NOT_FOUND, ex.getMessage());
    }

    /**
     * Один или несколько кодов провайдера отсутствуют в registry --> 400.
     */
    @ExceptionHandler(ProviderCodesNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleProviderCodesNotFound(ProviderCodesNotFoundException ex) {
        log.warn("Provider codes do not exist: {}", ex.getMessage());
        return ResponseEntityFactory.badRequest(PROVIDER_CODES_NOT_FOUND, ex.getMessage());
    }

    /**
     * План источников уже существует --> 409.
     */
    @ExceptionHandler(InstrumentSourcePlanAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleInstrumentSourcePlanAlreadyExists(
            InstrumentSourcePlanAlreadyExistsException ex
    ) {
        log.warn("Instrument source plan already exists: {}", ex.getMessage());
        return ResponseEntityFactory.conflict(INSTRUMENT_SOURCE_PLAN_ALREADY_EXISTS, ex.getMessage());
    }

    /**
     * План источников не найден --> 404.
     */
    @ExceptionHandler(InstrumentSourcePlanNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleInstrumentSourcePlanNotFound(InstrumentSourcePlanNotFoundException ex) {
        log.warn("Instrument source plan not found: {}", ex.getMessage());
        return ResponseEntityFactory.notFound(INSTRUMENT_SOURCE_PLAN_NOT_FOUND, ex.getMessage());
    }
}
