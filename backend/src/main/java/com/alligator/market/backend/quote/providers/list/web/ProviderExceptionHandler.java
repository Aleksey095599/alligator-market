package com.alligator.market.backend.quote.providers.list.web;

import com.alligator.market.backend.common.web.ApiResponse;
import com.alligator.market.backend.common.web.ResponseEntityFactory;
import com.alligator.market.backend.quote.providers.list.exceptions.DuplicateProviderException;
import com.alligator.market.backend.quote.providers.list.exceptions.ProviderNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Локальный обработчик исключений пакета «Provider».
 * Привязан к соответствующему контроллеру.
 */
@Slf4j
@RestControllerAdvice(assignableTypes = ProviderController.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ProviderExceptionHandler {

    /* Дублирование провайдера. */
    @ExceptionHandler(DuplicateProviderException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateProvider(
            DuplicateProviderException ex) {

        log.warn("DuplicateProviderException: {}", ex.getMessage());
        return ResponseEntityFactory.error(HttpStatus.CONFLICT, ex.getMessage());
    }

    /* Провайдер не найден. */
    @ExceptionHandler(ProviderNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleProviderNotFound(
            ProviderNotFoundException ex) {

        log.warn("ProviderNotFoundException: {}", ex.getMessage());
        return ResponseEntityFactory.notFound(ex.getMessage());
    }


}
