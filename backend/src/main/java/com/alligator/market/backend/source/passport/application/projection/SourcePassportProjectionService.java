package com.alligator.market.backend.source.passport.application.projection;

import com.alligator.market.backend.source.passport.application.projection.port.SourcePassportProjectionWritePort;
import com.alligator.market.backend.sourceplan.plan.application.port.SourcePlanStatusSyncPort;
import com.alligator.market.domain.source.passport.SourcePassport;
import com.alligator.market.domain.source.registry.SourceRegistry;
import com.alligator.market.domain.source.vo.SourceCode;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class SourcePassportProjectionService {
    private final SourceRegistry sourceRegistry;
    private final SourcePassportProjectionWritePort writePort;
    private final SourcePlanStatusSyncPort sourcePlanStatusSyncPort;
    private final TransactionTemplate tx;

    public SourcePassportProjectionService(
            SourceRegistry sourceRegistry,
            SourcePassportProjectionWritePort writePort,
            SourcePlanStatusSyncPort sourcePlanStatusSyncPort,
            TransactionTemplate tx
    ) {
        this.sourceRegistry = Objects.requireNonNull(sourceRegistry, "sourceRegistry must not be null");
        this.writePort = Objects.requireNonNull(writePort, "writePort must not be null");
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
        Map<SourceCode, SourcePassport> passportsFromRegistry = Map.copyOf(
                Objects.requireNonNull(sourceRegistry.passportsByCode(), "passportsByCode must not be null")
        );

        if (passportsFromRegistry.isEmpty()) {
            throw new IllegalStateException("Market source registry returned no source passports");
        }

        Set<SourceCode> passportsCodesFromRegistry = Set.copyOf(passportsFromRegistry.keySet());

        writePort.retireAllExcept(passportsCodesFromRegistry);
        writePort.upsertAll(passportsFromRegistry);

        sourcePlanStatusSyncPort.syncSourcePlanStatuses();
    }
}
