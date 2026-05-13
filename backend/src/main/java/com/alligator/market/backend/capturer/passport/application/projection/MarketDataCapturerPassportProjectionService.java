package com.alligator.market.backend.capturer.passport.application.projection;

import com.alligator.market.backend.sourceplan.plan.application.port.SourcePlanStatusSyncPort;
import com.alligator.market.domain.capturer.passport.CapturerPassport;
import com.alligator.market.domain.capturer.passport.registry.RuntimeCapturerPassportRegistry;
import com.alligator.market.domain.capturer.passport.registry.StoredCapturerPassportRegistry;
import com.alligator.market.domain.capturer.vo.CapturerCode;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class MarketDataCapturerPassportProjectionService {
    private final RuntimeCapturerPassportRegistry runtimePassportRegistry;
    private final StoredCapturerPassportRegistry storedPassportRegistry;
    private final SourcePlanStatusSyncPort sourcePlanStatusSyncPort;
    private final TransactionTemplate tx;

    public MarketDataCapturerPassportProjectionService(
            RuntimeCapturerPassportRegistry runtimePassportRegistry,
            StoredCapturerPassportRegistry storedPassportRegistry,
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
        Map<CapturerCode, CapturerPassport> runtimePassports = Map.copyOf(
                Objects.requireNonNull(runtimePassportRegistry.passportsByCode(), "passportsByCode must not be null")
        );

        if (runtimePassports.isEmpty()) {
            throw new IllegalStateException("Runtime capturer passport registry returned no capturer passports");
        }

        Set<CapturerCode> runtimePassportCodes = Set.copyOf(runtimePassports.keySet());

        storedPassportRegistry.retireAllExcept(runtimePassportCodes);
        storedPassportRegistry.saveActive(runtimePassports);

        sourcePlanStatusSyncPort.syncSourcePlanStatuses();
    }
}
