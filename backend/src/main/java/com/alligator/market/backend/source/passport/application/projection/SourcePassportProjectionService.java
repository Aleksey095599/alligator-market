package com.alligator.market.backend.source.passport.application.projection;

import com.alligator.market.backend.sourceplan.plan.application.port.SourcePlanStatusSyncPort;
import com.alligator.market.domain.source.passport.SourcePassport;
import com.alligator.market.domain.source.passport.registry.runtime.RuntimeSourcePassportRegistry;
import com.alligator.market.domain.source.passport.registry.stored.StoredSourcePassportRegistry;
import com.alligator.market.domain.source.vo.SourceCode;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class SourcePassportProjectionService {
    private final RuntimeSourcePassportRegistry runtimePassportRegistry;
    private final StoredSourcePassportRegistry storedPassportRegistry;
    private final SourcePlanStatusSyncPort sourcePlanStatusSyncPort;
    private final TransactionTemplate tx;

    public SourcePassportProjectionService(
            RuntimeSourcePassportRegistry runtimePassportRegistry,
            StoredSourcePassportRegistry storedPassportRegistry,
            SourcePlanStatusSyncPort sourcePlanStatusSyncPort,
            TransactionTemplate tx
    ) {
        this.runtimePassportRegistry = Objects.requireNonNull(
                runtimePassportRegistry,
                "runtimePassportRegistry must not be null"
        );
        this.storedPassportRegistry = Objects.requireNonNull(
                storedPassportRegistry,
                "storedPassportRegistry must not be null"
        );
        this.sourcePlanStatusSyncPort = Objects.requireNonNull(
                sourcePlanStatusSyncPort,
                "sourcePlanStatusSyncPort must not be null"
        );
        this.tx = Objects.requireNonNull(tx, "tx must not be null");
    }

    public void project() {
        tx.executeWithoutResult(status -> projectInTransaction());
    }

    private void projectInTransaction() {
        Map<SourceCode, SourcePassport> runtimePassports = Map.copyOf(
                Objects.requireNonNull(runtimePassportRegistry.passportsByCode(), "passportsByCode must not be null")
        );

        if (runtimePassports.isEmpty()) {
            throw new IllegalStateException("Runtime source passport registry returned no source passports");
        }

        Set<SourceCode> runtimePassportCodes = Set.copyOf(runtimePassports.keySet());

        storedPassportRegistry.retireAllExcept(runtimePassportCodes);
        storedPassportRegistry.saveActive(runtimePassports);

        sourcePlanStatusSyncPort.syncSourcePlanStatuses();
    }
}
