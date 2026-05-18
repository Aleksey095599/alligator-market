package com.alligator.market.backend.process.quotemonitor.application.instrument.sync.runner;

import com.alligator.market.backend.process.quotemonitor.application.instrument.sync.QuoteMonitorInstrumentSelectionRegistrySynchronizationService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.Objects;

@Slf4j
public final class QuoteMonitorInstrumentSelectionRegistrySynchronizationStartupRunner
        implements ApplicationRunner {
    private final QuoteMonitorInstrumentSelectionRegistrySynchronizationService service;

    public QuoteMonitorInstrumentSelectionRegistrySynchronizationStartupRunner(
            QuoteMonitorInstrumentSelectionRegistrySynchronizationService service
    ) {
        this.service = Objects.requireNonNull(service, "service must not be null");
    }

    @Override
    public void run(@NonNull ApplicationArguments args) {
        log.info("Quote monitor instrument selection registry synchronization started");

        try {
            service.synchronize();
            log.info("Quote monitor instrument selection registry synchronization finished");
        } catch (RuntimeException ex) {
            log.error("Quote monitor instrument selection registry synchronization failed", ex);
            throw ex;
        }
    }
}
