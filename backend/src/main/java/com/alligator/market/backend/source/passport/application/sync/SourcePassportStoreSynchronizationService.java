package com.alligator.market.backend.source.passport.application.sync;

import com.alligator.market.backend.sourceplan.plan.application.port.SourcePlanStatusSyncPort;
import com.alligator.market.domain.source.handler.passport.store.sync.SourceHandlerPassportStoreSynchronizer;
import com.alligator.market.domain.source.passport.store.sync.SourcePassportStoreSynchronizer;
import com.alligator.market.domain.sourceplan.registry.sync.RuntimeSourcePlanRegistryUpdater;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Objects;

public final class SourcePassportStoreSynchronizationService {
    private final SourcePassportStoreSynchronizer sourcePassportStoreSynchronizer;
    private final SourceHandlerPassportStoreSynchronizer sourceHandlerPassportStoreSynchronizer;
    private final SourcePlanStatusSyncPort sourcePlanStatusSyncPort;
    private final RuntimeSourcePlanRegistryUpdater runtimeSourcePlanRegistryUpdater;
    private final TransactionTemplate tx;

    public SourcePassportStoreSynchronizationService(
            SourcePassportStoreSynchronizer sourcePassportStoreSynchronizer,
            SourceHandlerPassportStoreSynchronizer sourceHandlerPassportStoreSynchronizer,
            SourcePlanStatusSyncPort sourcePlanStatusSyncPort,
            RuntimeSourcePlanRegistryUpdater runtimeSourcePlanRegistryUpdater,
            TransactionTemplate tx
    ) {
        this.sourcePassportStoreSynchronizer = Objects.requireNonNull(
                sourcePassportStoreSynchronizer,
                "sourcePassportStoreSynchronizer must not be null"
        );
        this.sourceHandlerPassportStoreSynchronizer = Objects.requireNonNull(
                sourceHandlerPassportStoreSynchronizer,
                "sourceHandlerPassportStoreSynchronizer must not be null"
        );
        this.sourcePlanStatusSyncPort = Objects.requireNonNull(
                sourcePlanStatusSyncPort,
                "sourcePlanStatusSyncPort must not be null"
        );
        this.runtimeSourcePlanRegistryUpdater = Objects.requireNonNull(
                runtimeSourcePlanRegistryUpdater,
                "runtimeSourcePlanRegistryUpdater must not be null"
        );
        this.tx = Objects.requireNonNull(tx, "tx must not be null");
    }

    public void synchronize() {
        tx.executeWithoutResult(status -> synchronizeInTransaction());
        runtimeSourcePlanRegistryUpdater.updateRuntimeRegistry();
    }

    private void synchronizeInTransaction() {
        sourcePassportStoreSynchronizer.synchronizeStoreFromSourceRegistry();
        sourceHandlerPassportStoreSynchronizer.synchronizeStoreFromSourceRegistry();
        sourcePlanStatusSyncPort.syncSourcePlanStatuses();
    }
}
