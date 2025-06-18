package com.alligator.market.backend.quotes.config.web;

import com.alligator.market.backend.common.web.dto.ApiResponse;
import com.alligator.market.backend.common.web.util.ResponseEntityFactory;
import com.alligator.market.backend.pairs.exceptions.PairNotFoundException;
import com.alligator.market.backend.quotes.config.exceptions.DuplicateFxPairStreamingConfigException;
import com.alligator.market.backend.quotes.config.exceptions.FxPairStreamingConfigNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/* Локальный обработчик исключений пакета «FxPairStreamingConfig». */
@Slf4j
@RestControllerAdvice(assignableTypes = FxPairStreamingConfigController.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class FxPairStreamingConfigRestExceptionHandler {

    /* Дублирование конфигурации для пары и провайдера. */
    @ExceptionHandler(DuplicateFxPairStreamingConfigException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicate(
            DuplicateFxPairStreamingConfigException ex) {

        log.warn("DuplicateFxPairStreamingConfigException: {}", ex.getMessage());
        return ResponseEntityFactory.error(HttpStatus.CONFLICT, ex.getMessage());
    }

    /* Конфигурация стрима не найдена. */
    @ExceptionHandler(FxPairStreamingConfigNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(
            FxPairStreamingConfigNotFoundException ex) {

        log.warn("FxPairStreamingConfigNotFoundException: {}", ex.getMessage());
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
