package com.alligator.market.backend.provider.passport.application.projection;

import com.alligator.market.backend.provider.passport.application.projection.port.ProviderPassportProjectionWritePort;
import com.alligator.market.domain.provider.passport.ProviderPassport;
import com.alligator.market.domain.provider.registry.ProviderRegistry;
import com.alligator.market.domain.provider.vo.ProviderCode;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Use case сервис: построить/обновить проекцию паспортов провайдеров.
 *
 * <p>Здесь задаётся граница транзакции и выполняется синхронизация с доменным реестром.</p>
 */
public final class ProviderPassportProjectionService {

    private final ProviderRegistry providerRegistry;
    private final ProviderPassportProjectionWritePort writePort;
    private final TransactionTemplate tx;

    public ProviderPassportProjectionService(
            ProviderRegistry providerRegistry,
            ProviderPassportProjectionWritePort writePort,
            TransactionTemplate tx
    ) {
        this.providerRegistry = Objects.requireNonNull(providerRegistry, "providerRegistry must not be null");
        this.writePort = Objects.requireNonNull(writePort, "writePort must not be null");
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
        Map<ProviderCode, ProviderPassport> registryPassports = Map.copyOf(
                Objects.requireNonNull(providerRegistry.passportsByCode(), "passportsByCode must not be null")
        );

        // Инвариант доменной модели: реестр активных провайдеров не бывает пустым.
        if (registryPassports.isEmpty()) {
            throw new IllegalStateException("Provider registry returned no provider passports");
        }

        Set<ProviderCode> activeCodes = Set.copyOf(registryPassports.keySet());

        // Синхронизация состава и значений проекции с реестром.
        writePort.deleteAllExcept(activeCodes);
        writePort.upsertAll(registryPassports);
    }
}
