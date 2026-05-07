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
 * <p>Здесь задаётся граница транзакции и выполняется синхронизация с доменным реестром.</p>
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
     * Синхронизировать БД-проекцию паспортов провайдеров с доменным реестром.
     */
    public void project() {
        tx.executeWithoutResult(status -> projectInTransaction());
    }

    /**
     * Выполнить синхронизацию проекции в границах активной транзакции.
     */
    private void projectInTransaction() {
        Map<MarketDataSourceCode, MarketDataSourcePassport> registryPassports = Map.copyOf(
                Objects.requireNonNull(sourceRegistry.passportsByCode(), "passportsByCode must not be null")
        );

        // Инвариант доменной модели: реестр активных провайдеров не бывает пустым.
        if (registryPassports.isEmpty()) {
            throw new IllegalStateException("Market data source registry returned no source passports");
        }

        Set<MarketDataSourceCode> currentCodes = Set.copyOf(registryPassports.keySet());

        // Синхронизация состава и значений проекции с реестром.
        writePort.retireAllExcept(currentCodes);
        writePort.upsertAll(registryPassports);
        // Изменения lifecycle паспортов провайдеров могут сделать строки source plan устаревшими.
        sourceLifecycleStatusSyncPort.retireSourcesWithoutActiveSourcePassports();
    }
}
