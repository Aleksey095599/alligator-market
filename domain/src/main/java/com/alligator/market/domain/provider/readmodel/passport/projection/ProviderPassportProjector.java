package com.alligator.market.domain.provider.readmodel.passport.projection;

import com.alligator.market.domain.provider.model.passport.ProviderPassport;
import com.alligator.market.domain.provider.model.vo.ProviderCode;
import com.alligator.market.domain.provider.readmodel.passport.store.ProviderPassportStore;
import com.alligator.market.domain.provider.registry.ProviderRegistry;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Проекция паспортов провайдеров из реестра {@link ProviderRegistry} в хранилище {@link ProviderPassportStore}.
 */
public class ProviderPassportProjector {

    private final ProviderRegistry providerRegistry;
    private final ProviderPassportStore.Read readStore;
    private final ProviderPassportStore.Write writeStore;

    /**
     * Конструктор.
     *
     * @param providerRegistry Реестр провайдеров
     * @param readStore Хранилище паспортов (порты для чтения)
     * @param writeStore Хранилище паспортов (порты для записи)
     */
    public ProviderPassportProjector(
            ProviderRegistry providerRegistry,
            ProviderPassportStore.Read readStore,
            ProviderPassportStore.Write writeStore
    ) {
        this.providerRegistry = Objects.requireNonNull(providerRegistry, "providerRegistry must not be null");
        this.readStore = Objects.requireNonNull(readStore, "readStore must not be null");
        this.writeStore = Objects.requireNonNull(writeStore, "writeStore must not be null");
    }

    public void project() {
        // Карта реестра: "код провайдера → паспорт провайдера"
        Map<ProviderCode, ProviderPassport> registryPassports = providerRegistry.passportsByCode();
        // Коды провайдеров в хранилище
        Set<ProviderCode> storeCodes = new LinkedHashSet<>(readStore.findAllCodes());

        // 1) Если реестр пуст, удаляем все записи из хранилища
        if (registryPassports.isEmpty()) {
            if (!storeCodes.isEmpty()) {
                writeStore.deleteByCodes(storeCodes);
            }
            return;
        }

        // 2) Удаляем из хранилища записи для кодов провайдеров, которых нет в карте реестра
        Set<ProviderCode> obsolete = new LinkedHashSet<>(storeCodes);
        obsolete.removeAll(registryPassports.keySet());
        if (!obsolete.isEmpty()) {
            writeStore.deleteByCodes(obsolete);
        }

        // 3) Обновляем записи в хранилище
        writeStore.upsertAll(registryPassports);
    }
}
