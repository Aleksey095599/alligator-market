package com.alligator.market.backend.source.passport.application.projection.runner;

import com.alligator.market.backend.source.passport.application.projection.MarketDataSourcePassportProjectionService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.Objects;

/**
 * Starts source passport projection synchronization on application startup.
 */
@Slf4j
public final class MarketDataSourcePassportProjectionStartupRunner implements ApplicationRunner {

    private final MarketDataSourcePassportProjectionService service;

    public MarketDataSourcePassportProjectionStartupRunner(MarketDataSourcePassportProjectionService service) {
        this.service = Objects.requireNonNull(service, "service must not be null");
    }

    @Override
    public void run(@NonNull ApplicationArguments args) {
        log.info("Market data source passport projection started");

        try {
            service.project();
            log.info("Market data source passport projection finished");
        } catch (RuntimeException ex) {
            log.error("Market data source passport projection failed", ex);
            throw ex; // fail-fast: срываем старт приложения
        }
    }
}
