package com.alligator.market.backend.source.passport.application.projection;

import com.alligator.market.backend.source.passport.application.projection.port.MarketDataSourcePassportProjectionWritePort;
import com.alligator.market.backend.sourceplan.plan.application.port.MarketDataSourceLifecycleStatusSyncPort;
import com.alligator.market.domain.source.passport.MarketDataSourcePassport;
import com.alligator.market.domain.source.registry.MarketDataSourceRegistry;
import com.alligator.market.domain.source.vo.MarketDataSourceCode;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Use case service for synchronizing source passport projection rows.
 *
 * <p>Defines the transaction boundary and synchronizes the DB projection with the domain registry.</p>
 */
public final class MarketDataSourcePassportProjectionService {

    private final MarketDataSourceRegistry sourceRegistry;
    private final MarketDataSourcePassportProjectionWritePort writePort;
    private final MarketDataSourceLifecycleStatusSyncPort sourceLifecycleStatusSyncPort;
    private final TransactionTemplate tx;

    public MarketDataSourcePassportProjectionService(
            MarketDataSourceRegistry sourceRegistry,
            MarketDataSourcePassportProjectionWritePort writePort,
            MarketDataSourceLifecycleStatusSyncPort sourceLifecycleStatusSyncPort,
            TransactionTemplate tx
    ) {
        this.sourceRegistry = Objects.requireNonNull(sourceRegistry, "sourceRegistry must not be null");
        this.writePort = Objects.requireNonNull(writePort, "writePort must not be null");
        this.sourceLifecycleStatusSyncPort = Objects.requireNonNull(
                sourceLifecycleStatusSyncPort,
                "sourceLifecycleStatusSyncPort must not be null"
        );
        this.tx = Objects.requireNonNull(tx, "tx must not be null");
    }

    /**
     * Synchronizes market data source passport projection rows with the domain registry.
     */
    public void project() {
        tx.executeWithoutResult(status -> projectInTransaction());
    }

    /**
     * Runs projection synchronization inside the active transaction.
     */
    private void projectInTransaction() {
        Map<MarketDataSourceCode, MarketDataSourcePassport> registryPassports = Map.copyOf(
                Objects.requireNonNull(sourceRegistry.passportsByCode(), "passportsByCode must not be null")
        );

        // Domain invariant: the active source registry must not be empty.
        if (registryPassports.isEmpty()) {
            throw new IllegalStateException("Market data source registry returned no source passports");
        }

        Set<MarketDataSourceCode> currentCodes = Set.copyOf(registryPassports.keySet());

        // Synchronize projection membership and values with the registry.
        writePort.retireAllExcept(currentCodes);
        writePort.upsertAll(registryPassports);
        // Source passport lifecycle changes can retire source plan entries.
        sourceLifecycleStatusSyncPort.retireSourcesWithoutActiveSourcePassports();
    }
}
