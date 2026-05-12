package com.alligator.market.backend.source.passport.application.projection.runner;

import com.alligator.market.backend.source.passport.application.projection.SourcePassportProjectionService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.Objects;

@Slf4j
public final class SourcePassportProjectionStartupRunner implements ApplicationRunner {
    private final SourcePassportProjectionService service;

    public SourcePassportProjectionStartupRunner(SourcePassportProjectionService service) {
        this.service = Objects.requireNonNull(service, "service must not be null");
    }

    @Override
    public void run(@NonNull ApplicationArguments args) {
        log.info("Market source passport projection started");

        try {
            service.project();
            log.info("Market source passport projection finished");
        } catch (RuntimeException ex) {
            log.error("Market source passport projection failed", ex);
            throw ex;
        }
    }
}
