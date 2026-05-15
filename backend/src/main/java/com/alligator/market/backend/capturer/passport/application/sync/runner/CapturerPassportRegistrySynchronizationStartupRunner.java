package com.alligator.market.backend.capturer.passport.application.sync.runner;

import com.alligator.market.backend.capturer.passport.application.sync.CapturerPassportRegistrySynchronizationService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.Objects;

@Slf4j
public final class CapturerPassportRegistrySynchronizationStartupRunner implements ApplicationRunner {
    private final CapturerPassportRegistrySynchronizationService service;

    public CapturerPassportRegistrySynchronizationStartupRunner(
            CapturerPassportRegistrySynchronizationService service
    ) {
        this.service = Objects.requireNonNull(service, "service must not be null");
    }

    @Override
    public void run(@NonNull ApplicationArguments args) {
        log.info("Capturer passport registry synchronization started");

        try {
            service.synchronize();
            log.info("Capturer passport registry synchronization finished");
        } catch (RuntimeException ex) {
            log.error("Capturer passport registry synchronization failed", ex);
            throw ex;
        }
    }
}
