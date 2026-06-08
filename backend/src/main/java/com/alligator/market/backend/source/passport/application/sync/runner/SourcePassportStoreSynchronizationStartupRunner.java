package com.alligator.market.backend.source.passport.application.sync.runner;

import com.alligator.market.backend.source.passport.application.sync.SourcePassportStoreSynchronizationService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.Objects;

@Slf4j
public final class SourcePassportStoreSynchronizationStartupRunner implements ApplicationRunner {
    private final SourcePassportStoreSynchronizationService service;

    public SourcePassportStoreSynchronizationStartupRunner(
            SourcePassportStoreSynchronizationService service
    ) {
        this.service = Objects.requireNonNull(service, "service must not be null");
    }

    @Override
    public void run(@NonNull ApplicationArguments args) {
        log.info("Source passport store synchronization started");

        try {
            service.synchronize();
            log.info("Source passport store synchronization finished");
        } catch (RuntimeException ex) {
            log.error("Source passport store synchronization failed", ex);
            throw ex;
        }
    }
}
