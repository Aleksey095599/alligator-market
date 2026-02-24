package com.alligator.market.backend.provider.readmodel.passport.projection.startup;

import com.alligator.market.domain.provider.readmodel.passport.projection.ProviderPassportProjector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Objects;

/**
 * Запуск проекции паспортов провайдеров при старте приложения.
 */
@Slf4j
public final class ProviderPassportProjectionStartupRunner implements ApplicationRunner {

    private final ProviderPassportProjector projector;
    private final TransactionTemplate tx;

    public ProviderPassportProjectionStartupRunner(
            ProviderPassportProjector projector,
            TransactionTemplate tx
    ) {
        this.projector = Objects.requireNonNull(projector, "projector must not be null");
        this.tx = Objects.requireNonNull(tx, "tx must not be null");
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("Provider passport projection started");

        try {
            tx.executeWithoutResult(status -> projector.project());
            log.info("Provider passport projection finished");
        } catch (RuntimeException ex) {
            log.error("Provider passport projection failed", ex);
            throw ex; // fail-fast: срываем старт приложения
        }
    }
}
