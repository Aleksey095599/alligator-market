package com.alligator.market.backend.marketdata.provider.readmodel.passport.projection.startup;

import com.alligator.market.backend.marketdata.provider.readmodel.passport.projection.service.ProviderPassportProjectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.Objects;

/**
 * Запуск проекции паспортов провайдеров при старте приложения.
 */
@Slf4j
public final class ProviderPassportProjectionStartupRunner implements ApplicationRunner {

    private final ProviderPassportProjectionService service;

    public ProviderPassportProjectionStartupRunner(ProviderPassportProjectionService service) {
        this.service = Objects.requireNonNull(service, "service must not be null");
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("Provider passport projection started");

        try {
            service.project();
            log.info("Provider passport projection finished");
        } catch (RuntimeException ex) {
            log.error("Provider passport projection failed", ex);
            throw ex; // fail-fast: срываем старт приложения
        }
    }
}
