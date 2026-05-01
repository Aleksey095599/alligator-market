package com.alligator.market.backend.provider.api.advice;

import com.alligator.market.backend.provider.api.passport.query.list.controller.PassportListController;
import com.alligator.market.domain.provider.exception.HandlerNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Feature-specific обработчик ошибок API для provider.
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(assignableTypes = PassportListController.class)
public class ProviderApiExceptionHandler {
    private static final HttpStatus HANDLER_NOT_FOUND_STATUS = HttpStatus.NOT_FOUND;
    private static final String HANDLER_NOT_FOUND_TITLE = "Provider handler not found";

    /**
     * Хендлер для инструмента не найден --> 404.
     */
    @ExceptionHandler(HandlerNotFoundException.class)
    public ProblemDetail handleHandlerNotFound(HandlerNotFoundException ex) {
        log.warn("Provider handler not found: {}", ex.getMessage());
        return buildProblemDetail(
                ex.getMessage(),
                ProviderApiErrorCode.PROVIDER_HANDLER_NOT_FOUND.code()
        );
    }

    /* Общий builder ProblemDetail для единообразного контракта ошибок provider feature. */
    private ProblemDetail buildProblemDetail(String detail, String errorCode) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HANDLER_NOT_FOUND_STATUS, detail);
        problemDetail.setTitle(HANDLER_NOT_FOUND_TITLE);
        problemDetail.setProperty("errorCode", errorCode);
        return problemDetail;
    }
}
