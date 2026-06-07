package com.alligator.market.backend.capturer.passport.application.sync;

import com.alligator.market.backend.sourceplan.plan.application.port.SourcePlanStatusSyncPort;
import com.alligator.market.domain.capturer.passport.store.sync.CapturerPassportStoreSynchronizer;
import com.alligator.market.domain.sourceplan.registry.sync.RuntimeSourcePlanRegistryUpdater;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Objects;

public final class CapturerPassportRegistrySynchronizationService {
    private final CapturerPassportStoreSynchronizer capturerPassportStoreSynchronizer;
    private final SourcePlanStatusSyncPort sourcePlanStatusSyncPort;
    private final RuntimeSourcePlanRegistryUpdater runtimeSourcePlanRegistryUpdater;
    private final TransactionTemplate tx;

    public CapturerPassportRegistrySynchronizationService(
            CapturerPassportStoreSynchronizer capturerPassportStoreSynchronizer,
            SourcePlanStatusSyncPort sourcePlanStatusSyncPort,
            RuntimeSourcePlanRegistryUpdater runtimeSourcePlanRegistryUpdater,
            TransactionTemplate tx
    ) {
        this.capturerPassportStoreSynchronizer = Objects.requireNonNull(
                capturerPassportStoreSynchronizer,
                "capturerPassportStoreSynchronizer must not be null"
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
        capturerPassportStoreSynchronizer.synchronize();
        sourcePlanStatusSyncPort.syncSourcePlanStatuses();
    }
}
