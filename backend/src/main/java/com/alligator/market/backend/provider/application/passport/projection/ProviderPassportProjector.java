package com.alligator.market.backend.provider.application.passport.projection;

import com.alligator.market.domain.provider.passport.ProviderPassport;
import com.alligator.market.backend.provider.application.passport.projection.port.out.ProviderPassportProjectionWritePort;
import com.alligator.market.domain.provider.registry.exception.ProviderRegistryEmptyException;
import com.alligator.market.domain.provider.vo.ProviderCode;
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
    private final ProviderPassportProjectionWritePort writePort;

    /**
     * Конструктор.
     *
     * @param providerRegistry реестр провайдеров (источник истины)
     * @param writePort write-порт проекции паспортов
     */
    public ProviderPassportProjector(
            ProviderRegistry providerRegistry,
            ProviderPassportProjectionWritePort writePort
    ) {
        this.providerRegistry = Objects.requireNonNull(providerRegistry, "providerRegistry must not be null");
        this.writePort = Objects.requireNonNull(writePort, "writePort must not be null");
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
        writePort.deleteAllExcept(activeCodes);
        writePort.upsertAll(registryPassports);
    }
}
