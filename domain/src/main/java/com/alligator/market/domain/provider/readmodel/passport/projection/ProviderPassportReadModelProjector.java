package com.alligator.market.domain.provider.readmodel.passport.projection;

import com.alligator.market.domain.provider.model.passport.ProviderPassport;
import com.alligator.market.domain.provider.model.vo.ProviderCode;
import com.alligator.market.domain.provider.registry.ProviderRegistry;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Проекция read-model паспортов провайдеров в хранилище.
 *
 * <p>Источник истины: {@link ProviderRegistry}.
 * Витрина: {@link ProviderPassportReadStore}/{@link ProviderPassportWriteStore}.</p>
 */
public class ProviderPassportReadModelProjector {

    private final ProviderRegistry providerRegistry;
    private final ProviderPassportReadStore readStore;
    private final ProviderPassportWriteStore writeStore;

    /**
     * Конструктор.
     *
     * @param providerRegistry Реестр провайдеров
     * @param readStore Хранилище паспортов (порты для чтения)
     * @param writeStore Хранилище паспортов (порты для записи)
     */
    public ProviderPassportReadModelProjector(
            ProviderRegistry providerRegistry,
            ProviderPassportReadStore readStore,
            ProviderPassportWriteStore writeStore
    ) {
        this.providerRegistry = Objects.requireNonNull(providerRegistry, "providerRegistry must not be null");
        this.readStore = Objects.requireNonNull(readStore, "readStore must not be null");
        this.writeStore = Objects.requireNonNull(writeStore, "writeStore must not be null");
    }

    public void project() {
        // Карта "код провайдера → паспорт провайдера" из реестра
        Map<ProviderCode, ProviderPassport> registryPassports = providerRegistry.passportsByCode();
        // Коды провайдеров из хранилища
        Set<ProviderCode> storeCodes = new LinkedHashSet<>(readStore.findAllCodes());

        // 1) Если реестр пуст, удаляем все записи из хранилища
        if (registryPassports.isEmpty()) {
            if (!storeCodes.isEmpty()) {
                writeStore.deleteByCodes(storeCodes);
            }
            return;
        }

        // 2) Если в хранилище есть паспорта, которых нет в реестре, удаляем их
        Set<ProviderCode> obsolete = new LinkedHashSet<>(storeCodes);
        obsolete.removeAll(registryPassports.keySet());
        if (!obsolete.isEmpty()) {
            writeStore.deleteByCodes(obsolete);
        }

        // 3) Обновляем записи в хранилище
        writeStore.upsertAll(registryPassports);
    }
}
