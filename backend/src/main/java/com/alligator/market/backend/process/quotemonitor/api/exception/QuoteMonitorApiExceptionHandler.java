package com.alligator.market.backend.process.quotemonitor.api.exception;

import com.alligator.market.backend.process.quotemonitor.api.instrument.controller.QuoteMonitorInstrumentController;
import com.alligator.market.backend.process.quotemonitor.application.instrument.exception.DuplicateQuoteMonitorInstrumentCodeException;
import com.alligator.market.backend.process.quotemonitor.application.instrument.exception.QuoteMonitorSourcePlanNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(assignableTypes = {
        QuoteMonitorInstrumentController.class
})
public class QuoteMonitorApiExceptionHandler {
    @ExceptionHandler(DuplicateQuoteMonitorInstrumentCodeException.class)
    public ProblemDetail duplicateInstrumentCode(DuplicateQuoteMonitorInstrumentCodeException ex) {
        log.warn("Duplicate quote monitor instrument code: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Duplicate quote monitor instrument code",
                ex.getMessage(),
                QuoteMonitorApiErrorCode.DUPLICATE_INSTRUMENT_CODE.code()
        );
    }

    @ExceptionHandler(QuoteMonitorSourcePlanNotFoundException.class)
    public ProblemDetail sourcePlanNotFound(QuoteMonitorSourcePlanNotFoundException ex) {
        log.warn("Quote monitor source plan not found: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Quote monitor source plan not found",
                ex.getMessage(),
                QuoteMonitorApiErrorCode.SOURCE_PLAN_NOT_FOUND.code()
        );
    }

    private ProblemDetail buildProblemDetail(HttpStatus status, String title, String detail, String errorCode) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        problemDetail.setProperty("errorCode", errorCode);
        return problemDetail;
    }
}
