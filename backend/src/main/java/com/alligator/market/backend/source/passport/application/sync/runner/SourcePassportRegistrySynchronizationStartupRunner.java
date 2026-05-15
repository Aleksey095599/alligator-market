package com.alligator.market.backend.source.passport.application.sync.runner;

import com.alligator.market.backend.source.passport.application.sync.SourcePassportRegistrySynchronizationService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.Objects;

@Slf4j
public final class SourcePassportRegistrySynchronizationStartupRunner implements ApplicationRunner {
    private final SourcePassportRegistrySynchronizationService service;

    public SourcePassportRegistrySynchronizationStartupRunner(
            SourcePassportRegistrySynchronizationService service
    ) {
        this.service = Objects.requireNonNull(service, "service must not be null");
    }

    @Override
    public void run(@NonNull ApplicationArguments args) {
        log.info("Source passport registry synchronization started");

        try {
            service.synchronize();
            log.info("Source passport registry synchronization finished");
        } catch (RuntimeException ex) {
            log.error("Source passport registry synchronization failed", ex);
            throw ex;
        }
    }
}
