package com.alligator.market.backend.provider.api.error;

import com.alligator.market.backend.provider.api.passport.PassportController;
import com.alligator.market.domain.provider.exception.HandlerNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Feature-specific обработчик ошибок API provider.
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(assignableTypes = PassportController.class)
public class ProviderApiExceptionHandler {

    /**
     * Хендлер для инструмента не найден --> 404.
     */
    @ExceptionHandler(HandlerNotFoundException.class)
    public ProblemDetail handleHandlerNotFound(HandlerNotFoundException ex) {
        log.warn("Provider handler not found: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.NOT_FOUND,
                "Provider handler not found",
                ex.getMessage(),
                ProviderApiErrorCodes.PROVIDER_HANDLER_NOT_FOUND.code()
        );
    }

    /* Общий builder ProblemDetail для единообразного контракта ошибок provider feature. */
    private ProblemDetail buildProblemDetail(HttpStatus status, String title, String detail, String errorCode) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        problemDetail.setProperty("errorCode", errorCode);
        return problemDetail;
    }
}
