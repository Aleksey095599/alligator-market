package com.alligator.market.backend.common.web.advice;

import com.alligator.market.backend.common.web.response.ApiResponse;
import com.alligator.market.backend.common.web.response.ResponseEntityFactory;
import com.alligator.market.domain.common.exception.DomainErrorCode;
import com.alligator.market.domain.provider.handler.exception.HandlerNotFoundException;
import com.alligator.market.domain.provider.registry.exception.ProviderCodeDuplicateException;
import com.alligator.market.domain.provider.registry.exception.ProviderDisplayNameDuplicateException;
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

    /**
     * Дублирование кода провайдера --> 409.
     */
    @ExceptionHandler(ProviderCodeDuplicateException.class)
    public ResponseEntity<ApiResponse<Void>> providerCodeDuplicate(ProviderCodeDuplicateException ex) {
        log.warn("Provider code duplicate: {}", ex.getMessage());
        return ResponseEntityFactory.conflict(DomainErrorCode.PROVIDER_CODE_DUPLICATE.name(), ex.getMessage());
    }

    /**
     * Дублирование отображаемого имени провайдера --> 409.
     */
    @ExceptionHandler(ProviderDisplayNameDuplicateException.class)
    public ResponseEntity<ApiResponse<Void>> providerDisplayNameDuplicate(ProviderDisplayNameDuplicateException ex) {
        log.warn("Provider display name duplicate: {}", ex.getMessage());
        return ResponseEntityFactory.conflict(DomainErrorCode.PROVIDER_DISPLAY_NAME_DUPLICATE.name(), ex.getMessage());
    }
}
