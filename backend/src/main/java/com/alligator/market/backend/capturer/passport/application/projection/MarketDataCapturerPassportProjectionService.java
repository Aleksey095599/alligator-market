package com.alligator.market.backend.capturer.passport.application.projection;

import com.alligator.market.backend.capturer.passport.application.projection.port.MarketDataCapturerPassportProjectionWritePort;
import com.alligator.market.backend.sourceplan.plan.application.port.MarketDataSourceLifecycleStatusSyncPort;
import com.alligator.market.domain.capturer.passport.MarketDataCapturerPassport;
import com.alligator.market.domain.capturer.registry.MarketDataCapturerRegistry;
import com.alligator.market.domain.capturer.vo.MarketDataCapturerCode;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class MarketDataCapturerPassportProjectionService {
    private final MarketDataCapturerRegistry registry;
    private final MarketDataCapturerPassportProjectionWritePort writePort;
    private final MarketDataSourceLifecycleStatusSyncPort sourceLifecycleStatusSyncPort;
    private final TransactionTemplate tx;

    public MarketDataCapturerPassportProjectionService(
            MarketDataCapturerRegistry registry,
            MarketDataCapturerPassportProjectionWritePort writePort,
            MarketDataSourceLifecycleStatusSyncPort sourceLifecycleStatusSyncPort,
            TransactionTemplate tx
    ) {
        this.registry = Objects.requireNonNull(registry, "registry must not be null");
        this.writePort = Objects.requireNonNull(writePort, "writePort must not be null");
        this.sourceLifecycleStatusSyncPort = Objects.requireNonNull(
                sourceLifecycleStatusSyncPort,
                "sourceLifecycleStatusSyncPort must not be null"
        );
        this.tx = Objects.requireNonNull(tx, "tx must not be null");
    }

    public void project() {
        tx.executeWithoutResult(status -> projectInTransaction());
    }

    private void projectInTransaction() {
        Map<MarketDataCapturerCode, MarketDataCapturerPassport> passportsFromRegistry = Map.copyOf(
                Objects.requireNonNull(registry.passportsByCode(), "passportsByCode must not be null")
        );

        if (passportsFromRegistry.isEmpty()) {
            throw new IllegalStateException("Capturer registry returned no capturer passports");
        }

        Set<MarketDataCapturerCode> passportCodesFromRegistry = Set.copyOf(passportsFromRegistry.keySet());

        writePort.retireAllExcept(passportCodesFromRegistry);
        writePort.upsertAll(passportsFromRegistry);

        // Retired capturer passports also retire source-plan entries that depend on them.
        sourceLifecycleStatusSyncPort.retireSourcesWithoutActiveMarketDataCapturerPassports();
    }
}
