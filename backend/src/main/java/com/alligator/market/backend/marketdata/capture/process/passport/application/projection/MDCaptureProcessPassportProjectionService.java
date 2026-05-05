package com.alligator.market.backend.marketdata.capture.process.passport.application.projection;

import com.alligator.market.backend.marketdata.capture.process.passport.application.projection.port.MDCaptureProcessPassportProjectionWritePort;
import com.alligator.market.domain.marketdata.capture.process.passport.MDCaptureProcessPassport;
import com.alligator.market.domain.marketdata.capture.process.registry.MDCaptureProcessRegistry;
import com.alligator.market.domain.marketdata.capture.process.vo.MDCaptureProcessCode;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Use case сервис: построить/обновить проекцию паспортов процессов фиксации рыночных данных.
 *
 * <p>Здесь задаётся граница транзакции и выполняется синхронизация с доменным реестром.</p>
 */
public final class MDCaptureProcessPassportProjectionService {

    private final MDCaptureProcessRegistry registry;
    private final MDCaptureProcessPassportProjectionWritePort writePort;
    private final TransactionTemplate tx;

    public MDCaptureProcessPassportProjectionService(
            MDCaptureProcessRegistry registry,
            MDCaptureProcessPassportProjectionWritePort writePort,
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
        Map<MDCaptureProcessCode, MDCaptureProcessPassport> registryPassports = Map.copyOf(
                Objects.requireNonNull(registry.passportsByCode(), "passportsByCode must not be null")
        );

        // Инвариант доменной модели: реестр активных процессов фиксации не бывает пустым.
        if (registryPassports.isEmpty()) {
            throw new IllegalStateException("Capture process registry returned no capture process passports");
        }

        Set<MDCaptureProcessCode> currentCodes = Set.copyOf(registryPassports.keySet());

        // Синхронизация состава и значений проекции с реестром.
        writePort.retireAllExcept(currentCodes);
        writePort.upsertAll(registryPassports);
    }
}
