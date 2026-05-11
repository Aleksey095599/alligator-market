package com.alligator.market.backend.source.passport.application.projection;

import com.alligator.market.backend.source.passport.application.projection.port.MarketDataSourcePassportProjectionWritePort;
import com.alligator.market.backend.sourceplan.plan.application.port.SourcePlanStatusSyncPort;
import com.alligator.market.domain.source.passport.MarketDataSourcePassport;
import com.alligator.market.domain.source.registry.MarketDataSourceRegistry;
import com.alligator.market.domain.source.vo.MarketDataSourceCode;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class MarketDataSourcePassportProjectionService {
    private final MarketDataSourceRegistry sourceRegistry;
    private final MarketDataSourcePassportProjectionWritePort writePort;
    private final SourcePlanStatusSyncPort sourcePlanStatusSyncPort;
    private final TransactionTemplate tx;

    public MarketDataSourcePassportProjectionService(
            MarketDataSourceRegistry sourceRegistry,
            MarketDataSourcePassportProjectionWritePort writePort,
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
        Map<MarketDataSourceCode, MarketDataSourcePassport> passportsFromRegistry = Map.copyOf(
                Objects.requireNonNull(sourceRegistry.passportsByCode(), "passportsByCode must not be null")
        );

        if (passportsFromRegistry.isEmpty()) {
            throw new IllegalStateException("Market data source registry returned no source passports");
        }

        Set<MarketDataSourceCode> passportsCodesFromRegistry = Set.copyOf(passportsFromRegistry.keySet());

        writePort.retireAllExcept(passportsCodesFromRegistry);
        writePort.upsertAll(passportsFromRegistry);

        sourcePlanStatusSyncPort.syncSourcePlanStatuses();
    }
}
