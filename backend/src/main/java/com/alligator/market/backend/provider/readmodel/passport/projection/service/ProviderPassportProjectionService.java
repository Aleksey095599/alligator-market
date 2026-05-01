package com.alligator.market.backend.provider.readmodel.passport.projection.service;

import com.alligator.market.backend.provider.application.passport.projection.ProviderPassportProjector;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Objects;

/**
 * Use case сервис: построить/обновить проекцию паспортов провайдеров.
 *
 * <p>Здесь задаётся граница транзакции для доменного процесса проекции.</p>
 */
public final class ProviderPassportProjectionService {

    private final ProviderPassportProjector projector;
    private final TransactionTemplate tx;

    public ProviderPassportProjectionService(
            ProviderPassportProjector projector,
            TransactionTemplate tx
    ) {
        this.projector = Objects.requireNonNull(projector, "projector must not be null");
        this.tx = Objects.requireNonNull(tx, "tx must not be null");
    }

    /**
     * Выполнить проекцию атомарно.
     */
    public void project() {
        tx.executeWithoutResult(status -> projector.project());
    }
}
