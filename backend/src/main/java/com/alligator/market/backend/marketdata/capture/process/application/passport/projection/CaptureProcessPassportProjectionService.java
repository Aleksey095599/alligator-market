package com.alligator.market.backend.marketdata.capture.process.application.passport.projection;

import com.alligator.market.backend.marketdata.capture.process.application.passport.projection.port.CaptureProcessPassportProjectionWritePort;
import com.alligator.market.domain.marketdata.capture.process.passport.CaptureProcessPassport;
import com.alligator.market.domain.marketdata.capture.process.registry.CaptureProcessRegistry;
import com.alligator.market.domain.marketdata.capture.process.vo.CaptureProcessCode;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Use case сервис: построить/обновить проекцию паспортов процессов фиксации рыночных данных.
 *
 * <p>Здесь задаётся граница транзакции и выполняется синхронизация с доменным реестром.</p>
 */
public final class CaptureProcessPassportProjectionService {

    private final CaptureProcessRegistry registry;
    private final CaptureProcessPassportProjectionWritePort writePort;
    private final TransactionTemplate tx;

    public CaptureProcessPassportProjectionService(
            CaptureProcessRegistry registry,
            CaptureProcessPassportProjectionWritePort writePort,
            TransactionTemplate tx
    ) {
        this.registry = Objects.requireNonNull(registry, "registry must not be null");
        this.writePort = Objects.requireNonNull(writePort, "writePort must not be null");
        this.tx = Objects.requireNonNull(tx, "tx must not be null");
    }

    /**
     * Синхронизировать БД-проекцию паспортов процессов фиксации с доменным реестром.
     */
    public void project() {
        tx.executeWithoutResult(status -> projectInTransaction());
    }

    /**
     * Выполнить синхронизацию проекции в границах активной транзакции.
     */
    private void projectInTransaction() {
        Map<CaptureProcessCode, CaptureProcessPassport> registryPassports = Map.copyOf(
                Objects.requireNonNull(registry.passportsByCode(), "passportsByCode must not be null")
        );

        // Инвариант доменной модели: реестр активных процессов фиксации не бывает пустым.
        if (registryPassports.isEmpty()) {
            throw new IllegalStateException("Capture process registry returned no capture process passports");
        }

        Set<CaptureProcessCode> activeCodes = Set.copyOf(registryPassports.keySet());

        // Синхронизация состава и значений проекции с реестром.
        writePort.deleteAllExcept(activeCodes);
        writePort.upsertAll(registryPassports);
    }
}
