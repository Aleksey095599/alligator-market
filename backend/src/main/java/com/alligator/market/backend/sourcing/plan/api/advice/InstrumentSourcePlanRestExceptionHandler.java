package com.alligator.market.backend.sourcing.plan.api.advice;

import com.alligator.market.backend.sourcing.plan.api.command.create.controller.CreateInstrumentSourcePlanController;
import com.alligator.market.backend.sourcing.plan.api.command.delete.controller.DeleteInstrumentSourcePlanController;
import com.alligator.market.backend.sourcing.plan.api.command.replace.controller.ReplaceInstrumentSourcePlanController;
import com.alligator.market.backend.sourcing.plan.api.query.get.controller.GetInstrumentSourcePlanController;
import com.alligator.market.backend.sourcing.plan.api.query.list.controller.ListInstrumentSourcePlansController;
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
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Feature-specific обработчик ошибок API для сценариев InstrumentSourcePlan.
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(assignableTypes = {
        CreateInstrumentSourcePlanController.class,
        DeleteInstrumentSourcePlanController.class,
        ReplaceInstrumentSourcePlanController.class,
        GetInstrumentSourcePlanController.class,
        ListInstrumentSourcePlansController.class,
        InstrumentSourcePlanOptionsQueryController.class
})
public class InstrumentSourcePlanRestExceptionHandler {

    /* Единые коды ошибок sourcing feature. */
    private static final String INSTRUMENT_CODE_NOT_FOUND = "INSTRUMENT_CODE_NOT_FOUND";
    private static final String PROVIDER_CODES_NOT_FOUND = "PROVIDER_CODES_NOT_FOUND";
    private static final String INSTRUMENT_SOURCE_PLAN_ALREADY_EXISTS = "INSTRUMENT_SOURCE_PLAN_ALREADY_EXISTS";
    private static final String INSTRUMENT_SOURCE_PLAN_NOT_FOUND = "INSTRUMENT_SOURCE_PLAN_NOT_FOUND";
    private static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    private static final String MALFORMED_REQUEST = "MALFORMED_REQUEST";

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
                INSTRUMENT_CODE_NOT_FOUND
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
                PROVIDER_CODES_NOT_FOUND
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
                INSTRUMENT_SOURCE_PLAN_ALREADY_EXISTS
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
                INSTRUMENT_SOURCE_PLAN_NOT_FOUND
        );
    }

    /**
     * Ошибки валидации входного DTO (@Valid) --> 400.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.warn("Request validation failed: {}", ex.getMessage());

        ProblemDetail problemDetail = buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                "Request validation failed",
                VALIDATION_ERROR
        );

        /* Поля с ошибками -> компактная карта field -> message. */
        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> Objects.requireNonNullElse(fieldError.getDefaultMessage(),
                                "Invalid value"),
                        (first, second) -> first
                ));
        problemDetail.setProperty("fieldErrors", fieldErrors);

        return problemDetail;
    }

    /**
     * Некорректное тело запроса (JSON parse / type mismatch) --> 400.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.warn("Malformed request body: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Malformed request",
                "Request body is malformed or unreadable",
                MALFORMED_REQUEST
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
