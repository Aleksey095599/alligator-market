package com.alligator.market.backend.instrument.application.registry.sync.runner;

import com.alligator.market.backend.instrument.application.registry.sync.InstrumentRegistrySynchronizationService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.Objects;

@Slf4j
public final class InstrumentRegistrySynchronizationStartupRunner implements ApplicationRunner {
    private final InstrumentRegistrySynchronizationService service;

    public InstrumentRegistrySynchronizationStartupRunner(
            InstrumentRegistrySynchronizationService service
    ) {
        this.service = Objects.requireNonNull(service, "service must not be null");
    }

    @Override
    public void run(@NonNull ApplicationArguments args) {
        log.info("Instrument runtime registry synchronization started");

        try {
            service.synchronize();
            log.info("Instrument runtime registry synchronization finished");
        } catch (RuntimeException ex) {
            log.error("Instrument runtime registry synchronization failed", ex);
            throw ex;
        }
    }
}
