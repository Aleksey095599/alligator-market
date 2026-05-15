package com.alligator.market.backend.source.passport.application.sync;

import com.alligator.market.backend.sourceplan.plan.application.port.SourcePlanStatusSyncPort;
import com.alligator.market.domain.source.passport.registry.sync.StoredSourcePassportRegistryUpdater;
import com.alligator.market.domain.sourceplan.registry.sync.RuntimeSourcePlanRegistryUpdater;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Objects;

public final class SourcePassportRegistrySynchronizationService {
    private final StoredSourcePassportRegistryUpdater storedSourcePassportRegistryUpdater;
    private final SourcePlanStatusSyncPort sourcePlanStatusSyncPort;
    private final RuntimeSourcePlanRegistryUpdater runtimeSourcePlanRegistryUpdater;
    private final TransactionTemplate tx;

    public SourcePassportRegistrySynchronizationService(
            StoredSourcePassportRegistryUpdater storedSourcePassportRegistryUpdater,
            SourcePlanStatusSyncPort sourcePlanStatusSyncPort,
            RuntimeSourcePlanRegistryUpdater runtimeSourcePlanRegistryUpdater,
            TransactionTemplate tx
    ) {
        this.storedSourcePassportRegistryUpdater = Objects.requireNonNull(
                storedSourcePassportRegistryUpdater,
                "storedSourcePassportRegistryUpdater must not be null"
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
        storedSourcePassportRegistryUpdater.updateStoredRegistry();
        sourcePlanStatusSyncPort.syncSourcePlanStatuses();
    }
}
