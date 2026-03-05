package com.alligator.market.domain.provider.readmodel.passport.projection.projector;

import com.alligator.market.domain.provider.model.passport.ProviderPassport;
import com.alligator.market.domain.provider.readmodel.passport.projection.port.ProviderPassportProjectionWritePort;
import com.alligator.market.domain.provider.registry.exception.ProviderRegistryEmptyException;
import com.alligator.market.domain.provider.model.vo.ProviderCode;
import com.alligator.market.domain.provider.registry.ProviderRegistry;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Проектор паспортов провайдеров: проецирует текущее состояние {@link ProviderRegistry} (источник истины) в проекцию
 * паспортов, записываемую через {@link ProviderPassportProjectionWritePort}.
 *
 * <p>Примечание: Транзакционность обеспечивает вызывающая сторона (application/use-case слой).</p>
 */
public class ProviderPassportProjector {

    private final ProviderRegistry providerRegistry;
    private final ProviderPassportProjectionWritePort writeStore;

    /**
     * Конструктор.
     *
     * @param providerRegistry реестр провайдеров (источник истины)
     * @param writeStore write-порт проекции паспортов
     */
    public ProviderPassportProjector(
            ProviderRegistry providerRegistry,
            ProviderPassportProjectionWritePort writeStore
    ) {
        this.providerRegistry = Objects.requireNonNull(providerRegistry, "providerRegistry must not be null");
        this.writeStore = Objects.requireNonNull(writeStore, "writeStore must not be null");
    }

    /**
     * Спроецировать текущее состояние {@link ProviderRegistry} в проекцию паспортов провайдеров.
     */
    public void project() {
        Map<ProviderCode, ProviderPassport> registryPassports = Map.copyOf(
                Objects.requireNonNull(providerRegistry.passportsByCode(), "passportsByCode must not be null")
        );

        // Инвариант доменной модели: реестр активных провайдеров не бывает пустым.
        if (registryPassports.isEmpty()) {
            throw new ProviderRegistryEmptyException();
        }

        Set<ProviderCode> activeCodes = Set.copyOf(registryPassports.keySet());

        // Синхронизация состава и значений проекции с реестром.
        writeStore.deleteAllExcept(activeCodes);
        writeStore.upsertAll(registryPassports);
    }
}
