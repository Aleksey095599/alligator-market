package com.alligator.market.domain.provider.readmodel.passport.projection;

import com.alligator.market.domain.provider.model.passport.ProviderPassport;
import com.alligator.market.domain.provider.exception.ProviderRegistryEmptyException;
import com.alligator.market.domain.provider.model.vo.ProviderCode;
import com.alligator.market.domain.provider.readmodel.passport.store.ProviderPassportProjectionWriteStore;
import com.alligator.market.domain.provider.registry.ProviderRegistry;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Проектор паспортов провайдеров: синхронизирует materialized view паспортов с {@link ProviderRegistry}.
 *
 * <p>Источник истины — реестр. Результат выполнения {@link #project()} — проекция, эквивалентная реестру.</p>
 *
 * <p>Рекомендуется выполнять вызов атомарно на стороне приложения (например, в транзакции).</p>
 */
public class ProviderPassportProjector {

    private final ProviderRegistry providerRegistry;
    private final ProviderPassportProjectionWriteStore writeStore;

    /**
     * Конструктор.
     *
     * @param providerRegistry реестр провайдеров (источник истины)
     * @param writeStore write-порт проекции паспортов
     */
    public ProviderPassportProjector(
            ProviderRegistry providerRegistry,
            ProviderPassportProjectionWriteStore writeStore
    ) {
        this.providerRegistry = Objects.requireNonNull(providerRegistry, "providerRegistry must not be null");
        this.writeStore = Objects.requireNonNull(writeStore, "writeStore must not be null");
    }

    /**
     * Выполнить синхронизацию проекции паспортов с текущим состоянием реестра.
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

        // Сначала upsert, затем "cleanup": так уменьшаем окно отсутствия активных записей,
        // если вызывающая сторона не обернула операцию в транзакцию.
        writeStore.upsertAll(registryPassports);
        writeStore.deleteAllExcept(activeCodes);
    }
}
