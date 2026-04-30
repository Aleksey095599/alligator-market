package com.alligator.market.backend.common.web.advice;

import com.alligator.market.backend.common.web.response.ApiResponse;
import com.alligator.market.backend.common.web.response.ResponseEntityFactory;
import com.alligator.market.domain.common.exception.DomainErrorCode;
import com.alligator.market.domain.provider.exception.HandlerNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Централизованный обработчик исключений, вызванных нарушением доменной логики приложения.
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class DomainExceptionHandler {

    /**
     * Обработчик инструмента не найден --> 404.
     */
    @ExceptionHandler(HandlerNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handlerNotFound(HandlerNotFoundException ex) {
        log.warn("Handler not found: {}", ex.getMessage());
        return ResponseEntityFactory.notFound(DomainErrorCode.HANDLER_NOT_FOUND.name(), ex.getMessage());
    }
}
