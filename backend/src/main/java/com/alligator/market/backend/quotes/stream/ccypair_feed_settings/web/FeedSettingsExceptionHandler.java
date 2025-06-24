package com.alligator.market.backend.quotes.stream.ccypair_feed_settings.web;

import com.alligator.market.backend.ccypairs.exceptions.PairNotFoundException;
import com.alligator.market.backend.common.web.dto.ApiResponse;
import com.alligator.market.backend.common.web.util.ResponseEntityFactory;
import com.alligator.market.backend.quotes.stream.ccypair_feed_settings.exceptions.DuplicateSettingsException;
import com.alligator.market.backend.quotes.stream.ccypair_feed_settings.exceptions.SettingsNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Локальный обработчик исключений. Привязан к соответствующему контроллеру.
 */
@Slf4j
@RestControllerAdvice(assignableTypes = FeedSettingsController.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class FeedSettingsExceptionHandler {

    /* Дублирование конфигурации для пары и провайдера. */
    @ExceptionHandler(DuplicateSettingsException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicate(
            DuplicateSettingsException ex) {

        log.warn("DuplicateSettingsException: {}", ex.getMessage());
        return ResponseEntityFactory.error(HttpStatus.CONFLICT, ex.getMessage());
    }

    /* Конфигурация стрима не найдена. */
    @ExceptionHandler(SettingsNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(
            SettingsNotFoundException ex) {

        log.warn("SettingsNotFoundException: {}", ex.getMessage());
        return ResponseEntityFactory.notFound(ex.getMessage());
    }

    /* Валютная пара не найдена. */
    @ExceptionHandler(PairNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handlePairNotFound(
            PairNotFoundException ex) {

        log.warn("PairNotFoundException: {}", ex.getMessage());
        return ResponseEntityFactory.error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}
