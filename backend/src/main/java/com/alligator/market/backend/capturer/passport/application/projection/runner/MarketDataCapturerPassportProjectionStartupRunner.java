package com.alligator.market.backend.capturer.passport.application.projection.runner;

import com.alligator.market.backend.capturer.passport.application.projection.MarketDataCapturerPassportProjectionService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.Objects;

/**
 * Запуск проекции паспортов процессов захвата рыночных данных при старте приложения.
 */
@Slf4j
public final class MarketDataCapturerPassportProjectionStartupRunner implements ApplicationRunner {

    private final MarketDataCapturerPassportProjectionService service;

    public MarketDataCapturerPassportProjectionStartupRunner(MarketDataCapturerPassportProjectionService service) {
        this.service = Objects.requireNonNull(service, "service must not be null");
    }

    @Override
    public void run(@NonNull ApplicationArguments args) {
        log.info("Capturer passport projection started");

        try {
            service.project();
            log.info("Capturer passport projection finished");
        } catch (RuntimeException ex) {
            log.error("Capturer passport projection failed", ex);
            throw ex; // fail-fast: срываем старт приложения
        }
    }
}
