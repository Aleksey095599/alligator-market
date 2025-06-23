package com.alligator.market.backend.quotes.stream.providers.list.web;

import com.alligator.market.backend.common.web.dto.ApiResponse;
import com.alligator.market.backend.common.web.util.ResponseEntityFactory;
import com.alligator.market.backend.quotes.stream.providers.list.exceptions.DuplicateProviderException;
import com.alligator.market.backend.quotes.stream.providers.list.exceptions.ProviderNotFoundException;
import com.alligator.market.backend.quotes.stream.providers.list.exceptions.ProviderUsedInSettingsException;
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
public class ProviderRestExceptionHandler {

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

    /* Провайдер используется в настройках. */
    @ExceptionHandler(ProviderUsedInSettingsException.class)
    public ResponseEntity<ApiResponse<Void>> handleProviderUsed(
            ProviderUsedInSettingsException ex) {

        log.warn("ProviderUsedInSettingsException: {}", ex.getMessage());
        return ResponseEntityFactory.error(HttpStatus.CONFLICT, ex.getMessage());
    }

}
