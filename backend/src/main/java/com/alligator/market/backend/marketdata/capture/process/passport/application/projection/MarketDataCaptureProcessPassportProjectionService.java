package com.alligator.market.backend.marketdata.capture.process.passport.application.projection;

import com.alligator.market.backend.marketdata.capture.process.passport.application.projection.port.MarketDataCaptureProcessPassportProjectionWritePort;
import com.alligator.market.backend.sourceplan.plan.application.port.MarketDataSourceLifecycleStatusSyncPort;
import com.alligator.market.domain.marketdata.capture.process.passport.MarketDataCaptureProcessPassport;
import com.alligator.market.domain.marketdata.capture.process.registry.MarketDataCaptureProcessRegistry;
import com.alligator.market.domain.marketdata.capture.process.vo.MarketDataCaptureProcessCode;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Use case сервис: построить/обновить проекцию паспортов процессов захвата рыночных данных.
 *
 * <p>Здесь задаётся граница транзакции и выполняется синхронизация с доменным реестром.</p>
 */
public final class MarketDataCaptureProcessPassportProjectionService {

    private final MarketDataCaptureProcessRegistry registry;
    private final MarketDataCaptureProcessPassportProjectionWritePort writePort;
    private final MarketDataSourceLifecycleStatusSyncPort sourceLifecycleStatusSyncPort;
    private final TransactionTemplate tx;

    public MarketDataCaptureProcessPassportProjectionService(
            MarketDataCaptureProcessRegistry registry,
            MarketDataCaptureProcessPassportProjectionWritePort writePort,
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
        Map<MarketDataCaptureProcessCode, MarketDataCaptureProcessPassport> registryPassports = Map.copyOf(
                Objects.requireNonNull(registry.passportsByCode(), "passportsByCode must not be null")
        );

        // Инвариант доменной модели: реестр активных процессов захвата не бывает пустым.
        if (registryPassports.isEmpty()) {
            throw new IllegalStateException("Capture process registry returned no capture process passports");
        }

        Set<MarketDataCaptureProcessCode> currentCodes = Set.copyOf(registryPassports.keySet());

        // Синхронизация состава и значений проекции с реестром.
        writePort.retireAllExcept(currentCodes);
        writePort.upsertAll(registryPassports);
        // Изменения lifecycle паспортов процессов захвата могут сделать строки source plan устаревшими.
        sourceLifecycleStatusSyncPort.retireSourcesWithoutActiveCaptureProcessPassports();
    }
}
