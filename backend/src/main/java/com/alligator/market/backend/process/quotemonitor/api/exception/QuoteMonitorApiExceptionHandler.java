package com.alligator.market.backend.process.quotemonitor.api.exception;

import com.alligator.market.backend.process.quotemonitor.api.instrument.controller.QuoteMonitorInstrumentSelectionController;
import com.alligator.market.backend.process.quotemonitor.application.instrument.exception.QuoteMonitorInstrumentCandidateNotFoundException;
import com.alligator.market.backend.process.quotemonitor.application.instrument.exception.QuoteMonitorInstrumentSelectionLockedException;
import com.alligator.market.domain.process.quotemonitor.instrument.exception.DuplicateQuoteMonitorInstrumentCodeException;
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
        QuoteMonitorInstrumentSelectionController.class
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

    @ExceptionHandler(QuoteMonitorInstrumentCandidateNotFoundException.class)
    public ProblemDetail instrumentCandidateNotFound(QuoteMonitorInstrumentCandidateNotFoundException ex) {
        log.warn("Quote monitor instrument candidate not found: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Quote monitor instrument candidate not found",
                ex.getMessage(),
                QuoteMonitorApiErrorCode.INSTRUMENT_CANDIDATE_NOT_FOUND.code()
        );
    }

    @ExceptionHandler(QuoteMonitorInstrumentSelectionLockedException.class)
    public ProblemDetail instrumentSelectionLocked(QuoteMonitorInstrumentSelectionLockedException ex) {
        log.warn("Quote monitor instrument selection is locked: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.CONFLICT,
                "Quote monitor instrument selection is locked",
                ex.getMessage(),
                QuoteMonitorApiErrorCode.INSTRUMENT_SELECTION_LOCKED.code()
        );
    }

    private ProblemDetail buildProblemDetail(HttpStatus status, String title, String detail, String errorCode) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        problemDetail.setProperty("errorCode", errorCode);
        return problemDetail;
    }
}
