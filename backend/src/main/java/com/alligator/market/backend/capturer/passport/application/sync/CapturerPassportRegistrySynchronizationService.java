package com.alligator.market.backend.capturer.passport.application.sync;

import com.alligator.market.backend.sourceplan.plan.application.port.SourcePlanStatusSyncPort;
import com.alligator.market.domain.capturer.passport.registry.sync.StoredCapturerPassportRegistryUpdater;
import com.alligator.market.domain.sourceplan.registry.sync.RuntimeSourcePlanRegistryUpdater;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Objects;

public final class CapturerPassportRegistrySynchronizationService {
    private final StoredCapturerPassportRegistryUpdater storedCapturerPassportRegistryUpdater;
    private final SourcePlanStatusSyncPort sourcePlanStatusSyncPort;
    private final RuntimeSourcePlanRegistryUpdater runtimeSourcePlanRegistryUpdater;
    private final TransactionTemplate tx;

    public CapturerPassportRegistrySynchronizationService(
            StoredCapturerPassportRegistryUpdater storedCapturerPassportRegistryUpdater,
            SourcePlanStatusSyncPort sourcePlanStatusSyncPort,
            RuntimeSourcePlanRegistryUpdater runtimeSourcePlanRegistryUpdater,
            TransactionTemplate tx
    ) {
        this.storedCapturerPassportRegistryUpdater = Objects.requireNonNull(
                storedCapturerPassportRegistryUpdater,
                "storedCapturerPassportRegistryUpdater must not be null"
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
        storedCapturerPassportRegistryUpdater.updateStoredRegistry();
        sourcePlanStatusSyncPort.syncSourcePlanStatuses();
    }
}
