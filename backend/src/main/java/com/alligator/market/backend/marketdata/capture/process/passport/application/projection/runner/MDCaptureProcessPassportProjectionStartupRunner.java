package com.alligator.market.backend.marketdata.capture.process.passport.application.projection.runner;

import com.alligator.market.backend.marketdata.capture.process.passport.application.projection.MDCaptureProcessPassportProjectionService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.Objects;

/**
 * Запуск проекции паспортов процессов фиксации рыночных данных при старте приложения.
 */
@Slf4j
public final class MDCaptureProcessPassportProjectionStartupRunner implements ApplicationRunner {

    private final MDCaptureProcessPassportProjectionService service;

    public MDCaptureProcessPassportProjectionStartupRunner(MDCaptureProcessPassportProjectionService service) {
        this.service = Objects.requireNonNull(service, "service must not be null");
    }

    @Override
    public void run(@NonNull ApplicationArguments args) {
        log.info("Capture process passport projection started");

        try {
            service.project();
            log.info("Capture process passport projection finished");
        } catch (RuntimeException ex) {
            log.error("Capture process passport projection failed", ex);
            throw ex; // fail-fast: срываем старт приложения
        }
    }
}
