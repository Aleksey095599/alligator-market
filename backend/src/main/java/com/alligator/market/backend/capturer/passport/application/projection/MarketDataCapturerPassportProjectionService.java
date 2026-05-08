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

/**
 * Use case сервис: построить/обновить проекцию паспортов процессов захвата рыночных данных.
 *
 * <p>Здесь задаётся граница транзакции и выполняется синхронизация с доменным реестром.</p>
 */
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

    /**
     * Синхронизировать БД-проекцию паспортов процессов захвата с доменным реестром.
     */
    public void project() {
        tx.executeWithoutResult(status -> projectInTransaction());
    }

    /**
     * Выполнить синхронизацию проекции в границах активной транзакции.
     */
    private void projectInTransaction() {
        Map<MarketDataCapturerCode, MarketDataCapturerPassport> registryPassports = Map.copyOf(
                Objects.requireNonNull(registry.passportsByCode(), "passportsByCode must not be null")
        );

        // Инвариант доменной модели: реестр активных процессов захвата не бывает пустым.
        if (registryPassports.isEmpty()) {
            throw new IllegalStateException("Capturer registry returned no capturer passports");
        }

        Set<MarketDataCapturerCode> currentCodes = Set.copyOf(registryPassports.keySet());

        // Синхронизация состава и значений проекции с реестром.
        writePort.retireAllExcept(currentCodes);
        writePort.upsertAll(registryPassports);
        // Изменения lifecycle паспортов процессов захвата могут сделать строки source plan устаревшими.
        sourceLifecycleStatusSyncPort.retireSourcesWithoutActiveMarketDataCapturerPassports();
    }
}
